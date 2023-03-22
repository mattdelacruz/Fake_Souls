package a3.quadtree;

public class QuadTree {

    QuadTreePoint topLeft, botRight;
    QuadTreeNode node;
    QuadTree topLeftTree, topRightTree, botLeftTree, botRightTree;

    public QuadTree() {
        topLeft = new QuadTreePoint(0, 0);
        botRight = new QuadTreePoint(0, 0);
        topLeftTree = null;
        topRightTree = null;
        botLeftTree = null;
        botRightTree = null;
        node = null;
    }

    public QuadTree(QuadTreePoint topL, QuadTreePoint botR) {
        node = null;
        topLeft = topL;
        botRight = botR;
        topLeftTree = null;
        topRightTree = null;
        botLeftTree = null;
        botRightTree = null;
    }

    public void insert(QuadTreeNode n) {

        if (n == null) {
            return;
        }
        if (!inBounds(n.getPosition())) {
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
                            new QuadTreePoint((topLeft.z() + botRight.z()) / 2, (topLeft.x() + botRight.x()) / 2));
                }
                System.out.println("top left");
                topLeftTree.insert(n);
                // bottom left position
            } else {
                if (botLeftTree == null) {
                    botLeftTree = new QuadTree(
                            new QuadTreePoint(topLeft.z(), (topLeft.x() + botRight.x()) / 2),
                            new QuadTreePoint((topLeft.z() + botRight.z()) / 2, botRight.x()));
                }
                System.out.println("bot left");

                botLeftTree.insert(n);
            }
            // position is on the right side
        } else {
            // top right position
            if ((topLeft.x() + botRight.x()) / 2 <= n.getPosition().x()) {

                if (topRightTree == null) {
                    topRightTree = new QuadTree(new QuadTreePoint((topLeft.z() + botRight.z()) / 2, topLeft.x()),
                            new QuadTreePoint(botRight.z(), (topLeft.x() + botRight.x()) / 2));
                }
                System.out.println("top right");

                topRightTree.insert(n);

                // bottom right position
            } else {
                if (botRightTree == null) {
                    botRightTree = new QuadTree(
                            new QuadTreePoint((topLeft.z() + botRight.z()) / 2, (topLeft.x() + botRight.x()) / 2),
                            new QuadTreePoint(botRight.z(), botRight.x()));
                }
                System.out.println("bot right");

                botRightTree.insert(n);
            }
        }
    }

    public QuadTreeNode search(QuadTreePoint pos) {
        if (!inBounds(pos)) {
            System.out.println("Not inside the bounds");
            return null;
        }

        if (node != null) {
            return node;
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

    private boolean inBounds(QuadTreePoint p) {
        return (p.z() >= topLeft.z() && p.z() <= botRight.z() &&
                p.x() <= topLeft.x() && p.x() >= botRight.x());
    }

    public QuadTreeNode getNode() {
        return node;
    }
}
