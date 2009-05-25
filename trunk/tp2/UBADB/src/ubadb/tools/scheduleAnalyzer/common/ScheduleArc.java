package ubadb.tools.scheduleAnalyzer.common;


public class ScheduleArc {
	private String startTransaction;
	private String endTransaction;
	private int startIndex;
	private int endIndex;
	
	
	public ScheduleArc(String startTransaction, String endTransaction,int startIndex, int endIndex)
	{
		this.startTransaction = startTransaction;
		this.endTransaction = endTransaction;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public String getStartTransaction()
	{
		return startTransaction;
	}

	public String getEndTransaction()
	{
		return endTransaction;
	}

	public int getStartIndex()
	{
		return startIndex;
	}

	public int getEndIndex()
	{
		return endIndex;
	}
	
	@Override
	public boolean equals(Object arc) {
		return ((ScheduleArc)arc).getStartTransaction().equals(this.startTransaction) &&
		((ScheduleArc)arc).getEndTransaction().equals(this.endTransaction);
	}
	
	@Override
	public String toString() {
		return "(" + this.startTransaction + "," + this.endTransaction + ")";
	}
}
