package com.ostsoft.games.compresch;

import com.ostsoft.games.compresch.block.Block;
import com.ostsoft.games.compresch.block.LZ2;
import com.ostsoft.games.compresch.tree.CrunchTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BlockList {
    private final LinkedList<Block> mLst;
    private boolean empty;

    public BlockList() {
        this.mLst = new LinkedList<>();
    }


    public void insertBlock(Block block) {
        int index = 0;

//        int startI = mLst.size() / 2;
//        if (startI > 0) {
//            if (mLst.get(startI).start < block.start) {
//                for (int i = startI; i < mLst.size(); i++) {
//                    Block iteratorBlock = mLst.get(i);
//                    if (block.start <= iteratorBlock.start) {
//                        index = i;
//                        index--;
//                        if (index < 0) {
//                            index = 0;
//                        }
//                        break;
//                    }
//                }
//            }
//            else {
//                for (int i = startI; i > 0; i--) {
//                    Block iteratorBlock = mLst.get(i);
//                    if (block.start > iteratorBlock.start) {
//                        index = i;
//                        index--;
//                        if (index < 0) {
//                            index = 0;
//                        }
//                        break;
//                    }
//                }
//
//            }
//        }
//
//
//        mLst.add(index, block);
        mLst.add(block);
    }

    public void crunchList(SizeLimits[] typelimits, SizeLimits[] rawlimits, int endofdata) {
        Iterator<Block> iterator = mLst.iterator();
        while (iterator.hasNext()) {
            Block next = iterator.next();
            if (next instanceof LZ2) {
                if (((LZ2) next).getOffset() == LZ2.Offset.RELATIVE && ((LZ2) next).getLz2dat() == 0) {
                    iterator.remove();
                }
            }
        }

        Comparator<Block> blockComparator = (o1, o2) -> {
            if (o1.start < o2.start) {
                return -1;
            }
            else if (o1.start == o2.start) {
                if (o1.len < o2.len) {
                    return -1;
                }
                else if (o1.len == o2.len) {
                    return 0;
                }
                else {
                    return 1;
                }
            }
            else {
                return 1;
            }
        };
        Collections.sort(mLst, blockComparator);


//        System.out.println(mLst.size());
//        killPointlessBlocks();
//        System.out.println(mLst.size());

//        for (Block block : mLst) {
//            if (block.start <= 92 && block.start > 25) {
//                System.out.println(block + " - " + block.start);
//            }
//        }


        CrunchTree tree = new CrunchTree();
        tree.Crunch(this, typelimits, rawlimits, endofdata);

        System.out.println("BlockList test: " + test());

    }

    private boolean test() {
        boolean problem = false;

        for (Block block : mLst) {
            if (block.len == 1) {
                problem = true;
            }
        }


        return problem;
    }

    private void killPointlessBlocks() {
        // block A is truely pointless if another block B covers the same area equally.
        // 
        // This is true when all of the following are true:
        //      
        //    1)  A begins and ends inside B
        //    2)  B can be moved to start at the same point A starts at
        //    3)  B's body size is less than or equal to A's
        //
        // if any of those are false, then A may still be needed


        // ----------------------------------

        List<Block> removeList = new ArrayList<>();

        Iterator<Block> i = mLst.iterator();
        Iterator<Block> j = mLst.iterator();

        while (i.hasNext()) {
            Block iItem = i.next();
            while (j.hasNext()) {
                Block jItem = j.next();

                if (iItem == jItem) {
                    continue;
                }
                else if (jItem.start >= iItem.stop) {
                    // j has passed up what i covers, no need to continue
                    break;
                }

                // now see if i makes j pointless
                if (jItem.start < iItem.start) {
                    // j starts before i, not pointless
                    continue;
                }
                else if (jItem.stop > iItem.stop) {
                    // j ends after i
                    continue;
                }
                else if
                        (
                        (jItem.getBodySize() + jItem.hasExtraByte())
                                < (iItem.getBodySize() + jItem.hasExtraByte())
                        ) {
                    // j has smaller body than i
                    continue;
                }
                else if (!iItem.canShrink(jItem.start, iItem.stop)) {
                    // i can't start where j starts
                    continue;
                }

                // if we got here,
                // j is pointless!
//                j.remove();

                removeList.add(jItem);


                //delete jItem;
                //j = mLst.erase(j);

//                if (j != mLst.begin()) {
//                    --j;
//                }
            }
        }

        for (Block block : removeList) {
            mLst.remove(block);
        }
        System.out.println("Removed " + removeList.size() + " pointless blocks");
    }


    public int size() {
        return mLst.size();
    }

    public boolean isEmpty() {
        return mLst.isEmpty();
    }

    public LinkedList<Block> getMLst() {
        return mLst;
    }
}
