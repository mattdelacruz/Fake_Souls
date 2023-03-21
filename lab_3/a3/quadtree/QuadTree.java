package a3.quadtree;

public class QuadTree {

    Point topLeft, botRight;
    Node node;
    QuadTree topLeftTree, topRightTree, botLeftTree, botRightTree;

    public QuadTree() {
        topLeft = new Point(0, 0);
        botRight = new Point(0, 0);
        topLeftTree = null;
        topRightTree = null;
        botLeftTree = null;
        botRightTree = null;
    }

    public QuadTree(Point topL, Point botR) {
        topLeft = topL;
        botRight = botR;
        topLeftTree = null;
        topRightTree = null;
        botLeftTree = null;
        botRightTree = null;
    }

    public void insert(Node n) {

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
        }
        // position is on the left side
        if ((topLeft.z() + botRight.z()) / 2 > n.getPosition().z()) {
            // top left position
            if ((topLeft.x() + botRight.x()) / 2 < n.getPosition().x) {
                if (topLeftTree == null) {
                    topLeftTree = new QuadTree(
                            new Point(topLeft.z(), topLeft.x()),
                            new Point((topLeft.z() + botRight.z()) / 2, (topLeft.x() + botRight.x()) / 2));
                }
                topLeftTree.insert(n);
                // bottom left position
            } else {
                if (botLeftTree == null) {
                    botLeftTree = new QuadTree(
                            new Point(topLeft.z(), (topLeft.x() + botRight.x()) / 2),
                            new Point((topLeft.z() + botRight.z()) / 2, botRight.x()));
                }
                botLeftTree.insert(n);
            }
            // position is on the right side
        } else {
            // top right position
            if ((topLeft.x() + botRight.x()) / 2 < n.getPosition().x) {

                if (topRightTree == null) {
                    topRightTree = new QuadTree(new Point((topLeft.z() + botRight.z()) / 2, topLeft.x()),
                            new Point(botRight.z(), (topLeft.x() + botRight.x()) / 2));
                }
                topRightTree.insert(n);

                // bottom right position
            } else {
                if (botRightTree == null) {
                    botRightTree = new QuadTree(
                            new Point((topLeft.z() + botRight.z()) / 2, (topLeft.x() + botRight.x()) / 2),
                            new Point(botRight.z(), botRight.x()));
                }
                botRightTree.insert(n);
            }
        }
    }

    public Node search(Point pos) {
        if (!inBounds(pos)) {
            System.out.println("Not inside the bounds");
            return null;
        }

        if (node != null) {
            return node;
        }

        // position is on the left side
        if ((topLeft.z() + botRight.z()) / 2 > pos.z()) {
            // top left position
            if ((topLeft.x() + botRight.x()) / 2 < pos.x()) {
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
            if ((topLeft.x() + botRight.x()) / 2 < pos.x()) {

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

    private boolean inBounds(Point p) {
        return (p.x() >= topLeft.x() && p.x <= botRight.x() &&
                p.z() >= topLeft.z() && p.z() < botRight.z());
    }
}
