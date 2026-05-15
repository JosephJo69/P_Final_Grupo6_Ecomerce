package org.tree.engine.service;

import org.tree.engine.model.TreeNode;

public interface TreeService {

    TreeNode createRoot(String value);

    TreeNode getTree();
}