//** this part is auto generated dont touch this

// Revision: $Revision: 370 $
// Last modified: $Date: 2012-11-22 20:26:39 +0100 (Do, 22 Nov 2012) $
// Last modified by: $Author: KoRnHolio $

package tsxdk.entity;

//import teamspeak.TsContext;
import tsxdk.entity.meta.LibEntityState;
import tsxdk.entity.meta.StatefulEntity;
import tsxdk.parser.LibTsSym;
import tsxdk.parser.TsFieldStack;
import utility.reclaimable.Reclaimable;
import api.data.TsComplainDTI;
import api.data.TsEntityType;

public class TsComplain implements TsComplainDTI, Reclaimable, TsEntity, StatefulEntity {
	private int tcldbid = 0;
	private String tname = "";
	private int fcldbid = 0;
	private String fname = "";
	private String message = "";
	private Long timestamp = 0L;
	// **************
	
    private LibEntityState state;
    private int propHash;
	private int tsxDbId;
	private int tsxGlueId;

	public TsComplain() {
		state = LibEntityState.INITIAL;
	}

	@Override
	public void clearForReuse () {
		//do nothing, we rely on update () 
	}
	
	/* (non-Javadoc)
	 * @see teamspeak.Ts_ComplainInterface#getTcldbid()
	 */
	@Override
	public int getTcldbid() {
		return tcldbid;
	}

	public void setTcldbid(int tcldbid) {
		this.tcldbid = tcldbid;
	}

	/* (non-Javadoc)
	 * @see teamspeak.Ts_ComplainInterface#getTname()
	 */
	@Override
	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	/* (non-Javadoc)
	 * @see teamspeak.Ts_ComplainInterface#getFcldbid()
	 */
	@Override
	public int getFcldbid() {
		return fcldbid;
	}

	public void setFcldbid(int fcldbid) {
		this.fcldbid = fcldbid;
	}

	/* (non-Javadoc)
	 * @see teamspeak.Ts_ComplainInterface#getFname()
	 */
	@Override
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	/* (non-Javadoc)
	 * @see teamspeak.Ts_ComplainInterface#getMessage()
	 */
	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see teamspeak.Ts_ComplainInterface#getTimestamp()
	 */
	@Override
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long time) {
		this.timestamp = time;
	}
	

	@Override
	public void update(TsFieldStack stack) {
		//message ^ timestamp is slot !

		tcldbid = (Integer) stack.popFirst(LibTsSym.TCLDBID);
		tname = (String) stack.popFirst(LibTsSym.TNAME);
		fcldbid = (Integer) stack.popFirst(LibTsSym.FCLDBID);
		fname = (String) stack.popFirst(LibTsSym.FNAME);
		
		setUpdated();
	}

	@Override
	public void setUpdated() {
		state.setUpdatedState(this);
	}

	@Override
	public void setCreated() {
		state.setCreatedState(this);
	}

	@Override
	public void setTouched() {
		state.setTouchedState(this);
	}

	@Override
	public void setUnused() {
		state.setUnusedState(this);
	}
	
	@Override
	public void setInitial() {
		state.setInitialState(this);
	}

	@Override
	public LibEntityState getState() {
		return state;
	}
	
	@Override
	public void setState(LibEntityState state) {
		this.state = state;		
	}

	@Override
	public TsEntityType getType() {
		return TsEntityType.TSCOMPLAIN;
	}

	@Override
	public int getTsPropHash() {
		return propHash;
	}

	@Override
	public void setTsPropHash(String str) {
		this.propHash = str.hashCode();
	}

	@Override
	public Long getSlotID() {
		return timestamp;			//oh gott....
	}
	
	@Override
	public void updateSlotID(Object k) {
		timestamp = (Long) k;
	}

	@Override
	public void setTSXDBID(int id) {
		this.tsxDbId = id;
	}
	@Override
	public int getTSXDBID() {
		return this.tsxDbId;
	}
	
	@Override
	public int getGlueID() {
		return tsxGlueId;
	}
	
	@Override
	public void setGlueID(int id) {
		this.tsxGlueId = id;
	}

}