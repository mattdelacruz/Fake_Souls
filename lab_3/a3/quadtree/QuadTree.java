package a3.quadtree;

public class QuadTree {
    QuadTreePoint topLeft, botRight;
    QuadTreeNode node;
    QuadTree topLeftTree, topRightTree, botLeftTree, botRightTree;
    int level;

    public QuadTree() {
        topLeft = new QuadTreePoint(0, 0);
        botRight = new QuadTreePoint(0, 0);
        level = 0;
    }

    public QuadTree(QuadTreePoint topL, QuadTreePoint botR, int lvl) {
        level = lvl;
        topLeft = topL;
        botRight = botR;
    }

    public void insert(QuadTreeNode n) {
        if (n == null) {
            return;
        }

        if (node == null) {
            node = n;
            return;
        }

        // unit area check, cannot divide any further
        if (Math.abs(topLeft.z() - botRight.z()) <= 1 && Math.abs(topLeft.x() - botRight.x()) <= 1) {
            if (node == null) {
                node = n;
            }
            return;
        }
        // position is on the left side
        if ((topLeft.z() + botRight.z()) / 2 >= n.getPosition().z()) {
            // top left position
            if ((topLeft.x() + botRight.x()) / 2 <= n.getPosition().x()) {
                if (topLeftTree == null) {
                    topLeftTree = new QuadTree(
                            new QuadTreePoint(topLeft.z(), topLeft.x()),
                            new QuadTreePoint((topLeft.z() + botRight.z()) / 2, (topLeft.x() + botRight.x()) / 2),
                            level + 1);
                }
                topLeftTree.insert(n);
                // bottom left position
            } else {
                if (botLeftTree == null) {
                    botLeftTree = new QuadTree(
                            new QuadTreePoint(topLeft.z(), (topLeft.x() + botRight.x()) / 2),
                            new QuadTreePoint((topLeft.z() + botRight.z()) / 2, botRight.x()), level + 1);
                }
                botLeftTree.insert(n);
            }
            // position is on the right side
        } else {
            // top right position
            if ((topLeft.x() + botRight.x()) / 2 <= n.getPosition().x()) {
                if (topRightTree == null) {

                    topRightTree = new QuadTree(new QuadTreePoint((topLeft.z() + botRight.z()) / 2, topLeft.x()),
                            new QuadTreePoint(botRight.z(), (topLeft.x() + botRight.x()) / 2), level + 1);
                }
                topRightTree.insert(n);
                // bottom right position
            } else {
                if (botRightTree == null) {

                    botRightTree = new QuadTree(
                            new QuadTreePoint((topLeft.z() + botRight.z()) / 2, (topLeft.x() + botRight.x()) / 2),
                            new QuadTreePoint(botRight.z(), botRight.x()), level + 1);
                }
                botRightTree.insert(n);
            }
        }
    }

    public QuadTree search(QuadTreePoint pos) {
        if (!inBounds(pos)) {
            return null;
        }

        if (node != null) {
            return this;
        }
        // position is on the left side
        if ((topLeft.z() + botRight.z()) / 2 >= pos.z()) {
            // top left position
            if ((topLeft.x() + botRight.x()) / 2 <= pos.x()) {
                if (topLeftTree == null) {
                    return null;
                }
                return topLeftTree.search(pos);
                // bottom left position
            } else {
                if (botLeftTree == null) {
                    return null;
                }
                return botLeftTree.search(pos);
            }
            // position is on the right side
        } else {
            // top right position
            if ((topLeft.x() + botRight.x()) / 2 <= pos.x()) {

                if (topRightTree == null) {
                    return null;
                }
                return topRightTree.search(pos);
                // bottom right position
            } else {
                if (botRightTree == null) {
                    return null;
                }
                return botRightTree.search(pos);
            }
        }
    }

    public QuadTreeNode findNearby(QuadTreePoint targetPos, float nearestDistance, QuadTreeNode nearestNode) {
        if (node == null) {
            return nearestNode;
        }

        if (!inBounds(targetPos)) {
            return nearestNode;
        }

        if (isLeafNode()) {
            float distance = findDistance(targetPos, node.getPosition());
            if (distance <= nearestDistance || nearestDistance == -1) {
                nearestDistance = distance;
                nearestNode = node;
            }
            return nearestNode;
        }

        float distance = findDistance(targetPos, node.getPosition());
        if (distance <= nearestDistance || nearestDistance == -1) {
            nearestDistance = distance;
            nearestNode = node;
        }

        QuadTree childNode = getChildNode(targetPos);
        if (childNode != null) {
            return childNode.findNearby(targetPos, nearestDistance, nearestNode);
        }
        return nearestNode;

    }

    public QuadTree getChildNode(QuadTreePoint pos) {
        if ((topLeft.z() + botRight.z()) / 2 >= pos.z()) {
            // top left position
            if ((topLeft.x() + botRight.x()) / 2 <= pos.x()) {
                return topLeftTree;
                // bottom left position
            } else {
                return botLeftTree;
            }
            // position is on the right side
        } else {
            // top right position
            if ((topLeft.x() + botRight.x()) / 2 <= pos.x()) {
                return topRightTree;
                // bottom right position
            } else {
                return botRightTree;
            }
        }
    }

    private float findDistance(QuadTreePoint a, QuadTreePoint b) {
        return (float) Math.sqrt(Math.pow(b.x() - a.x(), 2) + Math.pow(b.z() - a.z(), 2));
    }

    private boolean inBounds(QuadTreePoint p) {
        return (p.z() >= topLeft.z() && p.z() <= botRight.z() &&
                p.x() <= topLeft.x() && p.x() >= botRight.x());
    }

    public QuadTreeNode getNode() {
        return node;
    }

    public void setNode(QuadTreeNode n) {
        node = n;
    }

    public boolean isLeafNode() {
        return botLeftTree == null && botRightTree == null && topLeftTree == null && topRightTree == null;
    }

    public int getLevel() {
        return level;
    }
}
