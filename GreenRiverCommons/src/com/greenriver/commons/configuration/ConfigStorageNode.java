/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.configuration;

import com.greenriver.commons.tad.tree.NAryTreeNode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ConfigStorageNode implements NAryTreeNode<String, String> {

    private List<ConfigStorageNode> nodes;
    private String name;
    private String value;
    private ConfigStorageNode parent;
    private int capacity;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    public ConfigStorageNode getParent() {
        return parent;
    }

    public boolean isRoot() {
        return getParent() == null;
    }

    public boolean isLeaf() {
        return size() == 0;
    }

    public ConfigStorageNode(String name, int capacity) {
        nodes = new ArrayList<ConfigStorageNode>(capacity);
        this.capacity = capacity;
        this.name = name;
    }

    public ConfigStorageNode(String name) {
        this(name, 2);
    }

    public List<ConfigStorageNode> childs() {
        return nodes;
    }

    public int size() {
        return nodes.size();
    }

    public int getCapacity() {
        return capacity;
    }

    public ConfigStorageNode clone() {
        ConfigStorageNode newNode = new ConfigStorageNode(
                new String(name),
                capacity);
        
        for (ConfigStorageNode configStorageNode : nodes) {
            newNode.nodes.add(configStorageNode.clone());
        }

        return newNode;
    }

    public void addChild(int pos, ConfigStorageNode child) {
        if (pos < 0 || nodes.size() < pos || pos >= capacity) {
            throw new IndexOutOfBoundsException();
        }

        nodes.add(pos, child);
    }

    public void addChild(ConfigStorageNode child) {
        if (child.parent != null) {
            child.parent.removeChild(child);
        }

        child.parent = this;
        addChild(nodes.size(), child);
    }

    public void removeChild(ConfigStorageNode child) {
        nodes.remove(child);
    }

    void clear() {
        nodes.clear();
        parent = null;
    }

    ConfigStorageNode find(String substring) {
        for (ConfigStorageNode configStorageNode : nodes) {
            if (configStorageNode.getName().equalsIgnoreCase(substring)) {
                return configStorageNode;
            }
        }

        return null;
    }
}
