package org.tree.engine.service.impl;

import java.util.UUID;

import org.tree.engine.model.TreeNode;
import org.tree.engine.service.TreeService;

public class InMemoryTreeService implements TreeService {

    private TreeNode root;

    @Override
    public TreeNode createRoot(String value) {

        root = new TreeNode(
                UUID.randomUUID().toString(),
                value
        );

        return root;
    }

    @Override
    public TreeNode getTree() {

        return root;
    }
}