package utility.profiler;

import java.util.ArrayList;
import java.util.List;

import utility.profiler.info.AverageTimeProfileinfo;
import utility.profiler.info.HighestAverageTimeProfileinfo;
import utility.profiler.info.HighestTimeLapseProfileInfo;
import utility.profiler.info.LowestAverageTimeProfileInfo;
import utility.profiler.info.LowestTimeLapseProfileInfo;
import utility.profiler.info.RunCounterProfileInfo;
import utility.profiler.info.TimeLapseProfileInfo;
import utility.profiler.info.TotalRuntimePercentageProfileInfo;
import utility.profiler.info.TotalTimeLapseProfileInfo;

public class AdvancedProfiler {
	private static final class SingletonHolder {
		private static final AdvancedProfiler INSTANCE = new AdvancedProfiler();
	}

	public static final AdvancedProfiler getSharedInstance() {
		return SingletonHolder.INSTANCE;
	}

	private final List<ProfileSet> profilerList = new ArrayList<>();

	public ProfileSet createProfileSet(String name) {
		for (ProfileSet set : profilerList) {
			if (set.getName().equals(name)) {
				return set;
			}
		}

		final ProfileSet result = new ProfileSet(name);
		profilerList.add(result);
		populateProfileSet(result);
		return result;
	}

	private void populateProfileSet(ProfileSet ps) {
		ps.addInfo(new RunCounterProfileInfo("Run-count"));
		ps.addInfo(new LowestAverageTimeProfileInfo("Lowest-Average"));
		ps.addInfo(new AverageTimeProfileinfo("Latest-Average"));
		ps.addInfo(new HighestAverageTimeProfileinfo("Highest-Average"));
		ps.addInfo(new LowestTimeLapseProfileInfo("Lowest-Timelapse"));
		ps.addInfo(new TimeLapseProfileInfo("Latest-Timelapse"));
		ps.addInfo(new HighestTimeLapseProfileInfo("Highest-Timelapse"));
		ps.addInfo(new TotalTimeLapseProfileInfo("Total-Timelapse"));
		ps.addInfo(new TotalRuntimePercentageProfileInfo("Runtime %"));
	}
}
