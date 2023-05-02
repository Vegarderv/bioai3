package no.oyvegard;

public class Main {

	public static void main(String[] args) {
		RunConfig config = new RunConfig();

		switch (config.runMode) {
			case OUTPUT_ALL:
				Utils.SegmentAllImagesBothTypes();
				break;
			case DEMO_PREDICT:
				Utils.SegmentImageForDemo();
				break;
			case COMPARE_NSGA_GA:
				break;
		}
	}

}
