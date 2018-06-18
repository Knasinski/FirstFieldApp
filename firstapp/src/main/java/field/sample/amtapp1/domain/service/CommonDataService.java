package field.sample.amtapp1.domain.service;

public interface CommonDataService 
{
	public String getInstances(String classId);
	public String getInstance(String classId, String instanceId);
	public String getLatest(String classId, String instanceId);
	public String getHistory(String classId, String instanceId);
	public String getRelations(String classId, String instanceId);
}