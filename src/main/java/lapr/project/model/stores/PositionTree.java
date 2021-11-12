package lapr.project.model.stores;

import lapr.project.model.Position;
import lapr.project.shared.tree.AVL;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PositionTree {
    AVL<Position> positionBinarySearchTree;

    public PositionTree() {
        this.positionBinarySearchTree = new AVL<Position>();
    }

    public boolean addPosition(Position position) {
        try {
            positionBinarySearchTree.insert(position);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Position getSmallestPosition() {
        return positionBinarySearchTree.smallestElement();
    }

    public Position getBiggestPosition() {
        return positionBinarySearchTree.biggestElement();
    }

    public int getSize() {
        return positionBinarySearchTree.size();
    }

    public Iterable<Position> getOrderList() {
        return positionBinarySearchTree.inOrder();
    }

    public Position getPosition(LocalDateTime date) {
       return positionBinarySearchTree.find(new Position(0, 0, 0, 0, 0, date));
    }

    public List<Position> getInOrderList() {

        Iterable<Position> dateIterable = getOrderList();
        List<Position> positionList = new ArrayList<>();
        dateIterable.iterator().forEachRemaining(positionList::add);

        return positionList;

    }
}
