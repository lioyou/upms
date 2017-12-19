package com.leecp.upms.common.base;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BaseService<Record,Example> {
	/**
	 * 统计记录的数量
	 * @param example 附加统计条件
	 * @return 统计结果
	 */
	int countByExample(Example example);
	
	/**
	 * 根据条件，删除记录
	 * @param example 附加条件约束
	 * @return 删除的数目
	 */
    int deleteByExample(Example example);
    
    /**
     * 根据对象Id删除记录
     * @param recordId 记录Id
     * @return 删除记录数
     */
    int deleteByPrimaryKey(Integer recordId);
    
    /**
     * 根据Ids字符串，批量删除记录
     * @param recordIds 含有多个Id的字符串
     * @return 删除记录数
     */
    int deleteByPrimaryKeys(String recordIds);
    
    /**
     * 添加记录
     * @param record 记录
     * @return 添加数目
     */
    int insert(Record record);
    
    /**
     * 添加超大文本数据
     * @param record 文本记录
     * @return 添加结果
     */
    int insertWithBLOBs(Record record);
    
    /**
     * 有选择添加数据
     * @param record 记录
     * @return 添加数目
     */
    int insertSelective(Record record);
    
    /**
     * 有选择的添加超大文本数据
     * @param record 文本记录
     * @return 添加结果
     */
    int insertSelectiveWithBLOBs(Record record);
    
    /**
     * 根据条件查询数据
     * @param example 查询条件
     * @return 记录列表
     */
    List<Record> selectByExample(Example example);
    
    /**
     * 根据条件查询获取第一个记录
     * @param example 查询条件
     * @return 记录
     */
	Record selectFirstOneByExample(Example example);
	
    /**
     * 根据条件查询超大文本
     * @param example 查询条件
     * @return 记录列表
     */
    List<Record> selectByExampleWithBLOBs(Example example);
    
    /**
     * 根据条件查询获取第一个拥有超大文本的记录
     * @param example 查询条件
     * @return 记录
     */
    Record selectFirstOneByExampleWithBLOBs(Example example);

    /**
     * 根据记录Id查询记录
     * @param recordId 记录Id
     * @return 记录
     */
    Record selectByPrimaryKey(Integer recordId);
    
    /**
     * 根据记录Id查询拥有超大文本的记录
     * @param recordId 记录Id
     * @return 记录
     */
    Record selectByPrimaryKeyWithBLOBs(Integer recordId);
    
    /**
     * 根据条件更新部分数据
     * @param record 记录
     * @param example 附加条件
     * @return 更新数目
     */
    int updateByExampleSelective(@Param("record") Record record, @Param("example") Example example);
    
    /**
     * 根据条件更新拥有超大文本的部分数据
     * @param record 记录
     * @param example 附加条件
     * @return 更新数目
     */ 
    int updateByExampleSelectiveWithBLOBs(@Param("record") Record record, @Param("example") Example example);

    /**
     * 根据条件更新全部数据
     * @param record 记录
     * @param example 附加条件
     * @return 更新数目
     */
    int updateByExample(@Param("record") Record record, @Param("example") Example example);

    /**
     * 根据条件更新拥有超大文本的全部数据
     * @param record 记录
     * @param example 附加条件
     * @return 更新数目
     */
    int updateByExampleWithBLOBs(@Param("record") Record record, @Param("example") Example example);
    
    
    /**
     * 根据条件更新部分数据
     * @param record 记录
     * @return 更新数目
     */
    int updateByPrimaryKeySelective(Record record);
    
    /**
     * 根据条件更新拥有超大文本的部分数据
     * @param record 记录
     * @return 更新数目
     */
    int updateByPrimaryKeySelectiveWithBLOBs(Record record);

    /**
     * 根据条件更新全部数据
     * @param record 记录
     * @return 更新数目
     */
    int updateByPrimaryKey(Record record);
    
    /**
     * 根据条件更新拥有超大文本的全部数据
     * @param record 记录
     * @return 更新数目
     */
    int updateByPrimaryKeyWithBLOBs(Record record);
    
    
    
    
    //分页相关服务接口
    /**
     * 根据条件分页查询记录
     * @param example 查询条件
     * @param pageNum 起始页
     * @param pageSize 每一页的数据量
     * @return 数据
     */
	List<Record> selectByExampleForStartPage(Example example, Integer pageNum, Integer pageSize);
	
    /**
     * 根据条件分页查询拥有超大文本的记录
     * @param example 查询条件
     * @param pageNum 起始页
     * @param pageSize 每一页的数据量
     * @return 数据
     */
	List<Record> selectByExampleWithBLOBsForStartPage(Example example, Integer pageNum, Integer pageSize);

	/**
	 * 根据条件分布查询记录
	 * @param example 查询条件
	 * @param offset 起始数据偏移位置
	 * @param limit 查询数目
	 * @return
	 */
	List<Record> selectByExampleForOffsetPage(Example example, Integer offset, Integer limit);
	
	/**
	 * 根据条件分布查询拥有超大文本的记录
	 * @param example 查询条件
	 * @param offset 起始数据偏移位置
	 * @param limit 查询数目
	 * @return
	 */
	List<Record> selectByExampleWithBLOBsForOffsetPage(Example example, Integer offset, Integer limit);
	
	/**
	 * 获取实际mapper对象的字节码
	 */
	void initMapper();

}
