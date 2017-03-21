package api.data;

public interface TsComplainDTI extends TsEntityObject{

	int getTcldbid();

	String getTname();

	int getFcldbid();

	String getFname();

	String getMessage();

	Long getTimestamp();

}