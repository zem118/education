package com.education.common.utils; 

import com.education.common.model.TreeData;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class TreeUtils {

    public static List<Integer> getParentIds(List<? extends TreeData> dataList, int parentId) {
        List<? extends TreeData> parentList = getParentList(dataList, parentId);
        return parentList.stream()
                .map(item -> ((TreeData) item).getId())
                .collect(Collectors.toList());
    }

    /**
     * 获取子类 的所有父类结合
     * @param dataList
     * @param parentId
     * @return
     */
    public static <T extends TreeData> List<T> getParentList(List<T> dataList, int parentId) {
        List<T> parentList = new ArrayList<>();
        return getParent(dataList, parentId, parentList);
    }

    private static <T extends TreeData> List<T> getParent(List<T> dataList, int parentId, List<T> parentList) {
        for (T tree : dataList) {
            Integer id = tree.getId();
            Integer itemParentId = tree.getParentId();
            if (parentId == id) {
                parentList.add(tree);
                if (itemParentId != ResultCode.FAIL) {
                    return getParent(dataList, itemParentId, parentList);
                }
            }
        }
        return parentList;
    }

    public static <T extends TreeData> List<T> buildTreeData(List<T> dataList) {
        List<T> result = new ArrayList<>();
        List<T> parentList = getRoot(dataList);
        for (T tree : parentList) {
            List<T> children = getChildrenTree(dataList, tree.getId());
            if (ObjectUtils.isNotEmpty(children)) {
                tree.setChildren(children);
              //  tree.setHasChildren(true);
            }
            result.add(tree);
        }
        return result;
    }

    public static <T extends TreeData> List<T> getChildrenTree(List<T> dataList, int parentId) {
        List<T> childrenTree = new ArrayList();
        dataList.forEach(data -> {
            if (data.getParentId() == parentId) {
                childrenTree.add(data);
            }
        });
        childrenTree.forEach(data -> {
            List<? extends TreeData> children = getChildrenTree(dataList, data.getId());
            if (ObjectUtils.isNotEmpty(children)) {
                data.setChildren(children);
                //data.setHasChildren(true);
            }
        });
        return childrenTree;
    }

    /**
     * 获取顶级父类数据
     * @param dataList
     * @return
     */
    private static <T extends TreeData> List<T> getRoot(List<T> dataList) {
        List<T> rootList = new ArrayList();
        dataList.forEach(root -> {
            if (root.getParentId() == 0) {
                rootList.add(root);
            }
        });
        return rootList;
    }
}
