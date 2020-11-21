package avlTree;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AVLTreeTest {

    @Test
    void addTest() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            Set<Integer> controlSet = new HashSet<>();
            for (int j = 0; j < 15; j++) {
                int number = random.nextInt(100);
                while (controlSet.contains(number)) {
                    number = random.nextInt(100);
                }
                controlSet.add(number);
            }
            AVLTree<Integer> tree = new AVLTree<>();
            for (int element : controlSet) {
                tree.add(element);
                assertFalse(tree.add(element));
            }
            assertEquals(controlSet.size(), tree.size());
            assertTrue(tree.getRootHeight() == 4 || tree.getRootHeight() == 5);
            assertTrue(tree.getRootBalance() >= -1 && tree.getRootBalance() <= 1);
        }
    }

    @Test
    void removeTest() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            Set<Integer> controlSet = new HashSet<>();
            int removeIndex = random.nextInt(10);
            int toRemove = 0;
            for (int j = 0; j < 15; j++) {
                int number = random.nextInt(100);
                while (controlSet.contains(number)) {
                    number = random.nextInt(100);
                }
                controlSet.add(number);
                if (j == removeIndex)
                    toRemove = number;
            }
            AVLTree<Integer> tree = new AVLTree<>();
            assertFalse(tree.remove(toRemove));
            for (int element : controlSet) {
                tree.add(element);
            }
            controlSet.remove(toRemove);
            assertTrue(tree.remove(toRemove));
            assertFalse(tree.contains(toRemove));
            assertFalse(tree.remove(toRemove));
            assertEquals(controlSet.size(), tree.size());
            for (int element : controlSet)
                assertTrue(tree.contains(element));
            assertTrue(tree.getRootHeight() == 4 || tree.getRootHeight() == 5);
            assertTrue(tree.getRootBalance() >= -1 && tree.getRootBalance() <= 1);
        }
    }

    @Test
    void iteratorTest() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            Set<Integer> controlSet = new TreeSet<>();
            int removeIndex = random.nextInt(10);
            int toRemove = 0;
            for (int j = 0; j < 10; j++) {
                int number = random.nextInt(100);
                controlSet.add(number);
                if (j == removeIndex)
                    toRemove = number;
            }
            AVLTree<Integer> tree = new AVLTree<>();
            assertFalse(tree.iterator().hasNext());
            assertThrows(NoSuchElementException.class, () -> tree.iterator().next());
            assertThrows(IllegalStateException.class, () -> tree.iterator().remove());

            for (int element : controlSet)
                tree.add(element);
            Iterator<Integer> iterator = tree.iterator();
            Iterator<Integer> iterator1 = controlSet.iterator();
            while (iterator1.hasNext()) {
                assertEquals(iterator.next(), iterator1.next());
            }
            assertThrows(NoSuchElementException.class, iterator::next);

            controlSet.remove(toRemove);
            iterator = tree.iterator();
            int counter = tree.size();
            while (iterator.hasNext()) {
                int element = iterator.next();
                counter--;
                if (element == toRemove) {
                    iterator.remove();
                    assertThrows(IllegalStateException.class, iterator::remove);
                }
            }
            assertEquals(0, counter);
            assertEquals(controlSet.size(), tree.size());
            for (int element : controlSet)
                assertTrue(tree.contains(element));
            for (int element : tree)
                assertTrue(controlSet.contains(element));
        }
    }

    @Test
    void addAllRemoveAllTest() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            Set<Integer> controlSet = new HashSet<>();
            Set<Integer> removeIndexes = new HashSet<>();
            Set<Integer> removeSet = new HashSet<>();
            for (int j = 0; j < 8; j++) {
                int index = random.nextInt(15);
                while (removeIndexes.contains(index)) {
                    index = random.nextInt(15);
                }
                removeIndexes.add(index);
            }
            for (int j = 0; j < 15; j++) {
                int number = random.nextInt(100);
                while (controlSet.contains(number)) {
                    number = random.nextInt(100);
                }
                if (removeIndexes.contains(j))
                    removeSet.add(number);
                controlSet.add(number);
            }
            AVLTree<Integer> tree = new AVLTree<>();
            assertTrue(tree.addAll(controlSet));
            assertFalse(tree.addAll(controlSet));
            assertEquals(controlSet.size(), tree.size());
            assertTrue(tree.getRootHeight() == 4 || tree.getRootHeight() == 5);
            assertTrue(tree.getRootBalance() >= -1 && tree.getRootBalance() <= 1);


            controlSet.removeAll(removeSet);
            assertTrue(tree.containsAll(removeSet));
            assertTrue(tree.removeAll(removeSet));
            assertFalse(tree.removeAll(removeSet));
            assertFalse(tree.containsAll(removeSet));
            assertEquals(controlSet.size(), tree.size());
            for (int element : controlSet)
                assertTrue(tree.contains(element));
            assertTrue(tree.getRootHeight() == 4 || tree.getRootHeight() == 3);
            assertTrue(tree.getRootBalance() >= -1 && tree.getRootBalance() <= 1);
        }
    }

    @Test
    void toArrayTest() {
            Random random = new Random();
            for (int i = 0; i < 100; i++) {
                Object[] controlArray = new Object[15];
                Integer[] controlArray2 = new Integer[25];
                List<Integer> controlList = new ArrayList<>();
                for (int j = 0; j < 15; j++) {
                    int number = random.nextInt(100);
                    while (controlList.contains(number)) {
                        number = random.nextInt(100);
                    }
                    controlList.add(number);
                }
                Collections.sort(controlList);
                for (int j = 0; j < 15; j++) {
                    controlArray[j] = controlList.get(j);
                    controlArray2[j] = controlList.get(j);
                }
                AVLTree<Integer> tree = new AVLTree<>();
                tree.addAll(controlList);
                Object[] a = tree.toArray();
                assertArrayEquals(a, controlArray);
                Integer[] b = tree.toArray(new Integer[25]);
                assertArrayEquals(b, controlArray2);
                Integer[] c = tree.toArray(new Integer[1]);
                assertArrayEquals(c, controlArray);
                assertThrows(ArrayStoreException.class, () -> tree.toArray(new String[15]));
            }
    }

}