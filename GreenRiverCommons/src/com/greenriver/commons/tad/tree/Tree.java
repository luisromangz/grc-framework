/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.tad.tree;

/**
 * Generic tree
 * @param <IT>  Type of the id labels of the nodes of the tree
 * @param <N> Type of the nodes of the tree
 * @param <VT> Type of the values of the nodes of the tree
 * @author mangelp
 */
public interface Tree<IT, VT, N extends TreeNode<IT, VT>> {
    /**
     *
     * @return
     */
    public N getRoot();
}
