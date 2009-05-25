package ubadb.common;

/**
 * 
 * 
 * @author pfabrizio
 *
 */
public class PageIdentifier
{
	private int tableId;
	private int pageId;
	
	public PageIdentifier(int tableId, int pageId)
	{
		this.tableId = tableId;
		this.pageId = pageId;
	}

	public int getTableId()
	{
		return tableId;
	}

	public int getPageId()
	{
		return pageId;
	}
}
