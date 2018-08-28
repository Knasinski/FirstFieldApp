package field.sample.amtapp1.domain.service;

public interface CommonDataService 
{
	public StringBuffer getInstances(String classId);
	public StringBuffer getInstance(String classId, String instanceId);
	public StringBuffer getLatest(String classId, String instanceId);
	public StringBuffer getHistory(String classId, String instanceId);
	public StringBuffer getRelations(String classId, String instanceId);
	public StringBuffer getMoments(String classId, String instanceId);
	public int getCount(String classId, String instanceId);
}