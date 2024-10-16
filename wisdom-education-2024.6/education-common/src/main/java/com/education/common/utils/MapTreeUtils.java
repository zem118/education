package com.education.common.utils; 

import com.education.common.model.ModelBeanMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapTreeUtils {

    public static List<Integer> getParentIds(List<ModelBeanMap> dataList, int parentId) {
        List<ModelBeanMap> parentList = getParentList(dataList, parentId);
        return parentList.stream()
                .map(item -> item.getInt("value"))
                .collect(Collectors.toList());
    }

  
    public static List<ModelBeanMap> getParentList(List<ModelBeanMap> dataList, int parentId) {
        List<ModelBeanMap> parentList = new ArrayList<>();
        return getParent(dataList, parentId, parentList);
    }

    private static List<ModelBeanMap> getParent(List<ModelBeanMap> dataList, int parentId, List<ModelBeanMap> parentList) {
        for (ModelBeanMap data : dataList) {
            Integer id = data.getInt("value");
            if (ObjectUtils.isEmpty(id)) {
                id = data.getInt("id");
            }
            Integer itemParentId = data.getInt("parent_id");
            if (parentId == id) {
                parentList.add(data);
                if (itemParentId != ResultCode.FAIL) {
                    return getParent(dataList, itemParentId, parentList);
                }
            }
        }
        return parentList;
    }

    public static List<ModelBeanMap> buildTreeData(List<ModelBeanMap> dataList) {
        if (ObjectUtils.isEmpty(dataList))
            return null;
        List<ModelBeanMap> result = new ArrayList<>();
        List<ModelBeanMap> parentList = getRoot(dataList);
        for (ModelBeanMap tree : parentList) {
            setChildren(tree, dataList);
            result.add(tree);
        }
        return result;
    }

    public static List<ModelBeanMap> getChildrenTree(List<ModelBeanMap> dataList, int parentId) {
        List<ModelBeanMap> childrenTree = new ArrayList();
        dataList.forEach(data -> {
            if ((Integer)data.get("parent_id") == parentId) {
                childrenTree.add(data);
            }
        });
        childrenTree.forEach(data -> {
            setChildren(data, dataList);
        });
        return childrenTree;
    }

    private static void setChildren(ModelBeanMap data, List<ModelBeanMap> dataList) {
        Integer id = (Integer) data.get("value");
        if (ObjectUtils.isEmpty(id)) {
            id = (Integer) data.get("id");
        }
        List<ModelBeanMap> children = getChildrenTree(dataList, id);
        if (ObjectUtils.isNotEmpty(children)) {
            data.put("children", children);
        }
    }

    /**
     * 获取顶级父类数据
     * @param dataList
     * @return
     */
    private static List<ModelBeanMap> getRoot(List<ModelBeanMap> dataList) {
        List<ModelBeanMap> rootList = new ArrayList<>();
        dataList.forEach(root -> {
            if ((Integer)root.get("parent_id") == 0) {
                rootList.add(root);
            }
        });
        return rootList;
    }
}
