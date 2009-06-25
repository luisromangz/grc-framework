/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.tad.tree;

import java.util.Collection;

/**
 * Node for n-ary trees.
 *
 * This is a generic node in a tree where each node can have a name and a value.
 * 
 * @param <ID> 
 * @param <VT> node value type
 * @author mangelp
 */
public interface TreeNode<ID, VT> extends Cloneable {

    /**
     * Gets the name of this node
     * @return node name
     */
    public String getName();

    /**
     * Gets if this node is the root node for anything else
     * @return if this is the root node
     */
    public boolean isRoot();

    /**
     * Gets if this node is a key.
     * @return if this node is a leaf
     */
    public boolean isLeaf();

    /**
     *
     * @return the value of the node
     */
    public VT getValue();

    /**
     * Gets the getParent node or null if this node is the root
     * @param <T>
     * @return getParent node or null
     */
    public <T extends TreeNode<ID, VT>> T getParent();

    /**
     * Clones this node
     * @param <T> 
     * @return a clon of this node
     */
    public <T extends TreeNode<ID, VT>> T clone();
}
