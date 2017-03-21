package channelAdm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import api.data.EventDTI;
import api.data.LibFieldType;
import api.data.TsChannelDTI;
import api.data.TsClientDTI;
import api.plugin.EventClient;
import api.plugin.Plugin;
import api.query.AbstractQuery;
import api.service.BotServices;
import api.util.EntityPredicate;
import channelAdm.ChannelMarker;

/*
 * Anforderungen:
 * *Channel sollen zun�chst alphabetisch und anschlie�end nach UserCount sortiert werden
 * *SubChannel (Team1 .. Teamn) sollen automatisch erzeugt werden wenn Clients sich im entsprechenden Channel befinden
 * *Es soll immer ein leerer SubChannel zur Verf�gung stehen
 * *Die SubChannel sollen aktuell gehalten werden
 * *Die Channel werden nach anzahl der Clients sortiert
 * *Es soll max. ein leerer SubChannel pro Channel existieren
 */

/* Glossary:
 * 
 * MasterChannel : Contains Channels
 * Channel : Contains SubChannels and are sorted alphabetically and after usercount
 * SubChannel : Automatically generated, enumerated, named after default_subchannel_name, max. one free per Channel
 */


/* CVar suggestions:
 * 
 * - force_team_membership = true / false
 * - prefered_forced_team_selection = balanced / first
 */

public class ChannelAdm extends Plugin implements EventClient {
	private Integer[]					master_ids_to_sort;
	private int							subchannel_sort_delay;
	private String						default_subchannel_name;
	private Collection<TsChannelDTI>	channels;
	private Collection<TsClientDTI>		clients;

	private final String				SubChannelMarker;

	public ChannelAdm(BotServices service) {
		super("ChannelAdm", service);
		SubChannelMarker = service.encodeTS3String(ChannelMarker.AUTO_GENERATED.getValue());
	}

	@Override
	public EventClient[] getEventClients() {return new EventClient[] { this };}

	@Override
	protected void initConfig() {
		cVarUtil.registerCVar(cVarUtil.createArrayVar(createCVarID("master_ids_to_sort"), LibFieldType.CVAR_ARRAY_OF_INTEGER));
		cVarUtil.registerCVar(cVarUtil.createIntegerVar(createCVarID("subchannel_sort_delay"), 5000));
		cVarUtil.registerCVar(cVarUtil.createStringVar(createCVarID("default_subchannel_name"), "> Team"));
	}

	@Override
	public void onBrainBeat(EventDTI data) {
		master_ids_to_sort = cVarUtil.getCVar("ChannelAdm", "master_ids_to_sort").getValue();
		subchannel_sort_delay = cVarUtil.getCVar("ChannelAdm", "subchannel_sort_delay").getValue();
		default_subchannel_name = cVarUtil.getCVar("ChannelAdm", "default_subchannel_name").getValue();

		channels = channelUtil.getChannelIterable();
		clients = clientUtil.getClientIterable();
		
//		printChannelData(channels);

		for (int id : master_ids_to_sort) {
			processMasterChannel(id);
		}
	}
	
	private void printChannelData(Collection<TsChannelDTI> channelList) {
		for (TsChannelDTI current : channelList) {
			dbgOut(
					"CID: " + makeFixedLength(Integer.toString(current.getCid()), 5)
							+ "  PID: " + makeFixedLength(Integer.toString(current.getPid()), 5)
							+ "  Order: " + makeFixedLength(Integer.toString(current.getChannel_order()), 5)
							+ "  Total_clients: " + current.getTotal_clients()
							+ "  Total_clients_family: " + current.getTotal_clients_family()
							+ "  Name: " + current.getChannel_name()
					);
		}
	}

	
	private String makeFixedLength(String str, int len) {
		return String.format("%1$" + len + "s", str);
	}
	
	private String makeFixedLength(int str, int len) {
		return String.format("%1$" + len + "d", str);
	}

	private void processMasterChannel(int id) {
			orderChannelsFor(id);
			createSubChannelsFor(id);
	}

	private void orderChannelsFor(int pid) {
		final List<TsChannelDTI> channelsToOrder = channelUtil.getDirectSubChannels(channels, pid);
		final List<TsChannelDTI> restoredChannelOrder = channelUtil.mirrorChannelOrder(channelsToOrder);
		final List<TsChannelDTI> sortedChannelList = channelUtil.sortChannelsUserCount(channelUtil.sortChannelsAlphaNumeric(restoredChannelOrder));
		
		channelUtil.applyChannelOrder(sortedChannelList);
	}

	private void createSubChannelsFor(int pid) {
		final List<TsChannelDTI> channelsToCheck = channelUtil.getDirectSubChannels(channels, pid);

		final List<TsChannelDTI> channelsToDelete = new ArrayList<>();

		// Remove all subChannels whose parent-channel is empty (no members)
		// (Case 2)
		for (TsChannelDTI channel : channelsToCheck) {
			if (channel.getTotal_clients_family() == 0) {
				for (TsChannelDTI subChannel : channelUtil.getDirectSubChannels(channels, channel.getCid())) {
					if (subChannel.getChannel_topic().contains(SubChannelMarker))
						channelsToDelete.add(subChannel);
				}
			}
		}

		for (TsChannelDTI current : channelsToDelete) {
			channelsToCheck.remove(current);
			service.addAction(actionUtil.createQueryAction(0, 0, queryUtil.deleteChannel(current.getCid(), 1)));
			//was : service.addAction(service.createAction(0).channelDelete(current.getCid(), 1));
		}

		final List<TsChannelDTI> occupiedChannels = channelUtil.selectChannelsWhere(channelsToCheck,
				entity -> ((TsChannelDTI)entity).getTotal_clients_family() > 0);

		final List<TsChannelDTI> coveredChannels = new ArrayList<>(occupiedChannels.size());

		//Channel has members but no subchannels
		for (final TsChannelDTI channel : occupiedChannels) {
			if (!hasAutoSubChannels(channel.getCid())) {
				service.addAction(actionUtil.createQueryAction(0, 0, createAutoSubChannel(channel,1)));
				coveredChannels.add(channel);
			}
		}

		//Channel has members and one subchannel
		for (final TsChannelDTI channel : occupiedChannels) {
			final List<TsChannelDTI> autoSubChannels = getAutoSubChannels(channel.getCid());
			if (autoSubChannels.size() == 1 && channel.getTotal_clients() > 0) {
				service.addAction(actionUtil.createQueryAction(0, 0, createAutoSubChannel(channel,2)));

				final List<TsClientDTI> members = clientUtil.collectChannelMembers(clients, channel);

				service.addAction(actionUtil.createQueryAction(0, 0, queryUtil.moveClientsTo(autoSubChannels.get(0).getCid(), members)));

				coveredChannels.add(channel);
			}
		}

		// Ensure all channels with members have a free Channel
		occupiedChannels.removeAll(coveredChannels);

		for (TsChannelDTI channel : occupiedChannels) {
			applySubChannelContract(channel);
		}
	}

	private String createAutoChannelName(int index) {
		return service.encodeTS3String(default_subchannel_name + index);
	}

	private void applySubChannelContract(TsChannelDTI channel) {
		List<TsChannelDTI> subChannels = getAutoSubChannels(channel.getCid());

		//Select the subchannels that have clients in them in a list
		final List<TsChannelDTI> occupiedChannels =
				channelUtil.selectChannelsWhere(subChannels,
						new EntityPredicate<TsChannelDTI>() {
							@Override
							public boolean eval(TsChannelDTI entity) {
								return entity.getTotal_clients() > 0;
							}
						}
				);

		// Assure we have the right count of free and occupied channels
		//All subchannels are occupied
		if (subChannels.size() == occupiedChannels.size()) {
			service.addAction(actionUtil.createQueryAction(0, 0, createAutoSubChannel(channel, getNextFreeAutoChannelIndex(subChannels, 0))));
	    //We have more channels than necessary
		} else if (subChannels.size() > occupiedChannels.size() + 1 ) {
			//Save the channels we're about to delete in a list
			List<TsChannelDTI> channelsToDelete = new ArrayList<>(subChannels);
			channelsToDelete.removeAll(occupiedChannels);
			channelsToDelete = channelUtil.sortChannelsAlphaNumeric(channelsToDelete);
			channelsToDelete.remove(0);

			//Remove the channels
			/* *From our list for further processing
			 * *Per Query from the channellist on the server */
			for (TsChannelDTI current : channelsToDelete) {
				subChannels.remove(current);
				service.addAction(actionUtil.createQueryAction(0, 0, queryUtil.deleteChannel(current.getCid(), 1)));
				//was: service.addAction(service.createAction(0).channelDelete(current.getCid(), 1));
			}
		}

		// Now to assure we have a nice sequence of subchannels

		final List<TsChannelDTI> sortedSubChannels = channelUtil.sortChannelsAlphaNumeric(subChannels);

		int nextSlot = 1;

		for (TsChannelDTI current : sortedSubChannels) {
			if (current.getTotal_clients() == 0 &&  extractAutoChannelIndex(current.getChannel_name()) != nextSlot) {
				service.addAction(actionUtil.createQueryAction(0, 0, queryUtil.renameChannel(current.getCid(), createAutoChannelName(nextSlot))));
				//was: service.addAction(service.createAction(0).channelRename(current.getCid(), createAutoChannelName(nextSlot)));
			}
			nextSlot++;
		}

		channelUtil.applyChannelOrder(sortedSubChannels);
		
		//Notes:
		//On deleting a channel we need to reassure the channel-order
		//(hopefully DONE) We can not count on a correct channel order when recreating channel order - we need to take care of "in-betweeners"
	}

	private AbstractQuery createAutoSubChannel(TsChannelDTI parent, int subChannelIndex) {
		return queryUtil.createSubChannel(
				parent.getCid(),
				parent.getChannel_codec_quality(),
				createAutoChannelName(subChannelIndex),
				SubChannelMarker);
	}

	private int getNextFreeAutoChannelIndex(List<TsChannelDTI> subChannels, int beginSlot) {
		int ret = beginSlot + 1;

		for (TsChannelDTI current : channelUtil.sortChannelsAlphaNumeric(subChannels)) {
			if (ret == extractAutoChannelIndex(current.getChannel_name())) {
				ret++;
			}
			else {
				break;
			}
		}

		return ret;
	}

	private int extractAutoChannelIndex(final String cName) {
		return Integer.parseInt(cName.substring(cName.length() - 1));
	}

	private boolean hasAutoSubChannels(final int cid) {
		return getAutoSubChannels(cid).size() != 0;
	}

	private List<TsChannelDTI> getAutoSubChannels(final int cid) {
		return channelUtil.selectChannelsWhere(channels,
				entity -> ((TsChannelDTI)entity).getPid() == cid,
				entity -> ((TsChannelDTI)entity).getChannel_topic().contains(SubChannelMarker));
	}

	private void dbgOut (String str) {
		System.out.println(str);
	}
}
