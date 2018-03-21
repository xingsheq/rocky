package rocky.com.service;


import rocky.com.common.pojo.EUDataGridResult;
import rocky.com.common.pojo.TaotaoResult;
import rocky.com.pojo.TbItem;

public interface ItemService {

	TbItem getItemById(long itemId);
	EUDataGridResult getItemList(int page, int rows);
	TaotaoResult createItem(TbItem item, String desc, String itemParam) throws Exception;
}
