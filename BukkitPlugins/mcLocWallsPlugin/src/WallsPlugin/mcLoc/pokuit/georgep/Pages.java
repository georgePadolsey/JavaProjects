package WallsPlugin.mcLoc.pokuit.georgep;

import java.util.ArrayList;
import java.util.LinkedList;

public class Pages {

	private ArrayList<String> data;
	private int itemsPerPage;

	/**
	 * Constructor
	 * 
	 * @param arenaInfo
	 *            The list of information in a String array available to be
	 *            displayed
	 * @param itemsPerPage
	 *            How many items will be returned per page
	 */
	public Pages(ArrayList<String> arenaInfo, int itemsPerPage) {
		this.data = arenaInfo;
		this.itemsPerPage = Math.abs(itemsPerPage);
	}

	/**
	 * Returns the strings on a given page as a String[] array.
	 * 
	 * @param page
	 *            The page number to show (1 - pageNumbers)
	 * @return
	 */
	public String[] getStringsToSend(int page) {
		int startIndex = this.itemsPerPage * (Math.abs(page) - 1);
		LinkedList<String> list = new LinkedList<String>();
		if (page <= this.getPages()) {
			for (int i = startIndex; i < (startIndex + this.itemsPerPage); i++) {
				if (this.data.size() > i) {
					list.add(data.get(i));
				}
			}
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Get the number of pages which can be displayed.
	 * 
	 * @return
	 */
	public int getPages() {
		return (int) Math.ceil((double)data.size() / (double)this.itemsPerPage);
	}
	/**
	 *  Change data
	 *  @param data
	 *              The Data To Replace The Current Data
	 */
	public void changeData(ArrayList<String> data) {
		this.data = data;
	}

	/**
	 * 
	 * @return The number of elements in the data array.
	 */
	public int getRawArrayLength(){
		return this.data.size();
	}

}