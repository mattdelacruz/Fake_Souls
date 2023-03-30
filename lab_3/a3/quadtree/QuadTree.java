package a3.quadtree;

public class QuadTree {
    QuadTreePoint northWestCorner, southEastCorner;
    QuadTreeNode node;
    QuadTree northWestTree, northEastTree, southWestTree, southEastTree;

    public QuadTree() {
        northWestCorner = new QuadTreePoint(0, 0);
        southEastCorner = new QuadTreePoint(0, 0);
    }

    public QuadTree(QuadTreePoint northW, QuadTreePoint southE) {
        northWestCorner = northW;
        southEastCorner = southE;
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
        if (Math.abs(northWestCorner.z() - southEastCorner.z()) <= 1
                && Math.abs(northWestCorner.x() - southEastCorner.x()) <= 1) {
            if (node == null) {
                node = n;
            }
            return;
        }
        // position is on the left side
        if ((northWestCorner.z() + southEastCorner.z()) / 2 >= n.getPosition().z()) {
            // top left position
            if ((northWestCorner.x() + southEastCorner.x()) / 2 <= n.getPosition().x()) {
                if (northWestTree == null) {
                    northWestTree = new QuadTree(
                            new QuadTreePoint(northWestCorner.z(), northWestCorner.x()),
                            new QuadTreePoint((northWestCorner.z() + southEastCorner.z()) / 2,
                                    (northWestCorner.x() + southEastCorner.x()) / 2));
                }
                northWestTree.insert(n);
                // bottom left position
            } else {
                if (southWestTree == null) {
                    southWestTree = new QuadTree(
                            new QuadTreePoint(northWestCorner.z(), (northWestCorner.x() + southEastCorner.x()) / 2),
                            new QuadTreePoint((northWestCorner.z() + southEastCorner.z()) / 2, southEastCorner.x()));
                }
                southWestTree.insert(n);
            }
            // position is on the right side
        } else {
            // top right position
            if ((northWestCorner.x() + southEastCorner.x()) / 2 <= n.getPosition().x()) {
                if (northEastTree == null) {

                    northEastTree = new QuadTree(
                            new QuadTreePoint((northWestCorner.z() + southEastCorner.z()) / 2, northWestCorner.x()),
                            new QuadTreePoint(southEastCorner.z(), (northWestCorner.x() + southEastCorner.x()) / 2));
                }
                northEastTree.insert(n);
                // bottom right position
            } else {
                if (southEastTree == null) {

                    southEastTree = new QuadTree(
                            new QuadTreePoint((northWestCorner.z() + southEastCorner.z()) / 2,
                                    (northWestCorner.x() + southEastCorner.x()) / 2),
                            new QuadTreePoint(southEastCorner.z(), southEastCorner.x()));
                }
                southEastTree.insert(n);
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
        if ((northWestCorner.z() + southEastCorner.z()) / 2 >= pos.z()) {
            // top left position
            if ((northWestCorner.x() + southEastCorner.x()) / 2 <= pos.x()) {
                if (northWestTree == null) {
                    return null;
                }
                return northWestTree.search(pos);
                // bottom left position
            } else {
                if (southWestTree == null) {
                    return null;
                }
                return southWestTree.search(pos);
            }
            // position is on the right side
        } else {
            // top right position
            if ((northWestCorner.x() + southEastCorner.x()) / 2 <= pos.x()) {

                if (northEastTree == null) {
                    return null;
                }
                return northEastTree.search(pos);
                // bottom right position
            } else {
                if (southEastTree == null) {
                    return null;
                }
                return southEastTree.search(pos);
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
        if ((northWestCorner.z() + southEastCorner.z()) / 2 >= pos.z()) {
            // top left position
            if ((northWestCorner.x() + southEastCorner.x()) / 2 <= pos.x()) {
                return northWestTree;
                // bottom left position
            } else {
                return southWestTree;
            }
            // position is on the right side
        } else {
            // top right position
            if ((northWestCorner.x() + southEastCorner.x()) / 2 <= pos.x()) {
                return northEastTree;
                // bottom right position
            } else {
                return southEastTree;
            }
        }
    }

    private float findDistance(QuadTreePoint a, QuadTreePoint b) {
        return (float) Math.sqrt(Math.pow(b.x() - a.x(), 2) + Math.pow(b.z() - a.z(), 2));
    }

    private boolean inBounds(QuadTreePoint pos) {
        return (pos.z() >= northWestCorner.z() && pos.z() <= southEastCorner.z() &&
                pos.x() <= northWestCorner.x() && pos.x() >= southEastCorner.x());
    }

    private boolean isLeafNode() {
        return southWestTree == null && southEastTree == null &&
                northWestTree == null && northEastTree == null;
    }
}
