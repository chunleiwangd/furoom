package gpps.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import gpps.model.Binding;

public interface IBindingDao {
	public void create(Binding binding);
	public Binding find(Integer id);
	/**
	 * @param state 	-1表示不限状态
	 * @param userid	null表示不限用户
	 * */
	public List<Binding> findByTypeAndValueAndStateAndUserId(@Param("btype")int btype, @Param("tvalue")String tvalue, @Param("state")int state, @Param("userid")Integer userid);
	public void changeState(@Param("id")Integer id, @Param("state")int state);
}
