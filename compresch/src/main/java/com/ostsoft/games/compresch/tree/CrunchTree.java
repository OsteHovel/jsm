package com.ostsoft.games.compresch.tree;

import com.ostsoft.games.compresch.BlockList;
import com.ostsoft.games.compresch.SizeLimits;
import com.ostsoft.games.compresch.block.Block;

import java.util.Iterator;
import java.util.LinkedList;

public class CrunchTree {
    private SizeLimits[] pTypeLimits;
    private SizeLimits[] pRawLimits;

    private CrunchNode top;
    private LinkedList<CrunchNode> ends = new LinkedList<>();

    public int CalcBlockBytes(Block blk) {
        int coarse = blk.len / pTypeLimits[blk.type].lenMax;
        int fine = blk.len % pTypeLimits[blk.type].lenMax;

        // ---------------------------------

        coarse *= blk.getBodySize() + blk.hasExtraByte() + pTypeLimits[blk.type].blockSize + 1;

        if (fine > 0) { // FIXME: Unknown?
            coarse += blk.getBodySize() + blk.hasExtraByte() + pTypeLimits[blk.type].blockSize + ((fine > pTypeLimits[blk.type].extraByteAfter) ? 1 : 0);
        }

        return coarse;
    }

    public int CalcRawBytes(int len) {
        int type = 0;
        int coarse = len / pRawLimits[type].lenMax;
        int fine = len % pRawLimits[type].lenMax;

        // --------------------------------------

        coarse *= pRawLimits[type].lenMax + pRawLimits[type].blockSize + 1;

        if (fine > 0) { // FIXME: Unknown?
            coarse += fine + pRawLimits[type].blockSize + ((fine > pRawLimits[type].extraByteAfter) ? 1 : 0);
        }

        return coarse;
    }

    public void Crunch(BlockList lst, SizeLimits[] typelimits, SizeLimits[] rawlimits, int endofdata) {
        // empty list is empty
        if (lst.isEmpty()) {
            System.out.println("List is empty");
            return;
        }


        pTypeLimits = typelimits;
        pRawLimits = rawlimits;

        // clear the tree (if one exists -- it shouldn't, but just in case)
//        Destroy();

        // build tree from top down!
        top = new CrunchNode(null);
        ends.add(top);
        for (Block block : lst.getMLst()) {
            // pad and clean chains up to this block's starting point
            PadAndCleanChains(block.start);

            // then try putting this block in the chains
            ChainBlock(block);

            Test(lst, 1);
        }
        System.out.println("Ends size: " + ends.size());


        // after you do that with all the blocks, pad them to the end of data and clean
        PadAndCleanChains(endofdata);

        Test(lst, 2);

        // here, search 'ends' for the best chain
        CrunchNode best = ends.getFirst();
        for (CrunchNode jItem : ends) {
//        for (++j; j != ends.end(); ++j) {

            if (jItem.data.padBytes < best.data.padBytes) {
                best = jItem;
            }
            else if (jItem.data.padBytes == best.data.padBytes) {
                if (jItem.data.deep < best.data.deep) {
                    best = jItem;
                }
            }
        }


        Test(lst, 3);

        LinkedList<Block> bestList = new LinkedList<>();
        BuildBestList(bestList, best);

        Test(bestList, 4);

        // bestlist is now compiled -- kill the original list and replace it
        System.out.println("Yay: " + bestList.size());

//        for (CrunchNode end : ends) {
//            CrunchNode last = end.dn.getLast();
//            System.out.println(last);
//        }

        lst.getMLst().clear();
        lst.getMLst().addAll(bestList);

//        BlockList::EmptyList (lst);
//        lst.swap(bestList);

        Test(lst, 5);

        // and that's all she wrote!
    }

    private boolean Test(BlockList lst, int i) {
        return Test(lst.getMLst(), i);
    }

    private boolean Test(LinkedList<Block> lst, int i) {
        boolean problem = false;
        for (Block block : lst) {
            if (block.len == 1) {
                problem = true;
            }
        }

        if (problem) {
            System.out.println("PROBLEM IN HEAVEN at " + i);
        }
        return problem;
    }

    public CrunchNode Advance(CrunchNode i, LinkedList<CrunchNode> list) {
        if (i != list.getLast()) {
            Iterator<CrunchNode> iterator = list.iterator();
            while (iterator.hasNext()) {
                CrunchNode next = iterator.next();
                if (next == i) {
                    return iterator.next();
                }
            }
        }
        return null;
    }

    public void PadAndCleanChains(int start) {
        // this function takes the given starting point and checks it against each chain's ending point
        //   since the block list is stored in ascending order of starting points -- once we reach any given
        //   starting point... any chains that are not to the current starting point can only reach the point
        //   with a raw block.
        //
        // since raw blocks are big and ugly, this is where most chains will reach their dead end and be removed
        //   from the 'ends' list.

        // first... pad them all to starting point
        for (CrunchNode i : ends) {
//        for (i = ends.begin(); i != ends.end(); ++i) {
            if (i.data.padStop >= start)        // does not need padding
            {
                continue;
            }

            i.data.padBytes = i.data.bytes + CalcRawBytes(start - i.data.stop) - 1;
            i.data.padStop = start;
        }


        // now run through and clean bad chains
        //  a chain is bad if another chain reaches as far or further in as many or fewer bytes


        CrunchNode i, j;
        for (i = ends.getFirst(); i != null; i = Advance(i, ends)) {
            for (j = ends.getFirst(); (j != i) && (j != null); j = Advance(j, ends)) {

                if (i.data.stop == j.data.stop) {
                    if (i.data.bytes < j.data.bytes) {
                        j = removeNode(j, ends);
                    }
                    else if (j.data.bytes < i.data.bytes) {
                        i = removeNode(i, ends);
                        break;
                    }
                    else {
                        if (i.data.deep <= j.data.deep) {
                            j = removeNode(j, ends);
                        }
                        else {
                            i = removeNode(i, ends);
                            break;
                        }
                    }
                }
                else {
                    if (i.data.padStop != j.data.padStop) {
                        continue;
                    }

                    // Debug code
                    // {
                    if (ends.isEmpty()) {
                        continue;
                    }
                    else if (i == ends.getLast()) {
                        continue;
                    }
                    else if (j == ends.getLast()) {
                        continue;
                    }

                    CrunchNode c = j;

                    if (c.data.block == null) {
                        j = removeNode(j, ends);
                        continue;
                    }

                    int blockType = c.data.block.type;

                    if (i.data.padBytes <= (j.data.padBytes - pTypeLimits[blockType].blockSize - 1)) {
                        j = removeNode(j, ends);
                    }
                    else if (j.data.padBytes <= (i.data.padBytes - pTypeLimits[blockType].blockSize - 1)) {
                        i = removeNode(i, ends);
                        break;
                    }
                }
            }
        }
    }

    private CrunchNode removeNode(CrunchNode j, LinkedList<CrunchNode> ends) {
        CrunchNode jN = Advance(j, ends);
        ends.remove(j);
        return jN;
    }

    public void ChainBlock(Block blk) {
        // this is where we try putting 'block' in each chain (or rather where each chain is split by 'block')
        // note we only have to test against existing chains

        Block tmp;

        Crunch cr = new Crunch();
        int rem;

        cr.block = blk;

        CrunchNode i;

        for (i = ends.getFirst(); i != null; i = Advance(i, ends)) {
            // does this chain go further than this block does?  If so -- no point in adding this block to
            //  the chain

            if (i.data.padStop >= blk.stop) {
                continue;
            }

            // copy the block
            tmp = blk.dup();

            // does this chain go *into* this block?  If so -- try pushing this block forward so it starts where
            //  the chain left off
            if (i.data.padStop > blk.start) {
                if (tmp.shrink(i.data.padStop, tmp.stop))        // shrink failed, can't add to this chain
                {
//                    delete tmp;
                    continue;
                }
            }
            cr.start = tmp.start;

            if (cr.start == 27) {
                System.out.println("YA");
            }


            // now we're going to potentially fork several chains here.  There are many ways we need to attempt to apply
            // this block:
            //
            //    1)  In full (full len)
            //    2)  Just below extra byte boundaries (assuming it's above those bounds)
            //    3)  Just below extra block boundaries (assuming it's above those)

            //  testing for all 3 can be done in this neat little while loop:

            while (tmp.len > 0) {
                // in full, or just below extra block bound
                cr.stop = tmp.stop;
                cr.bytes = CalcBlockBytes(tmp) + i.data.padBytes;

                AddNode(i, cr);            // AddNode adds to the front of the list, so as not to disturb this for loop

                rem = tmp.len % pTypeLimits[tmp.type].lenMax;
                if (rem == 0) {
                    rem = pTypeLimits[tmp.type].lenMax;
                }

                if (rem > pTypeLimits[tmp.type].extraByteAfter) {
                    // just below extra byte bound
                    tmp.dropLen(tmp.len - rem + pTypeLimits[tmp.type].extraByteAfter);
                    cr.stop = tmp.stop;
                    cr.bytes = CalcBlockBytes(tmp) + i.data.padBytes;
                    AddNode(i, cr);

                    rem = tmp.len % pTypeLimits[tmp.type].lenMax;

                    if (rem == 0) {
                        rem = pTypeLimits[tmp.type].lenMax;
                    }
                }

                tmp.dropLen(tmp.len - rem);
            }

//            delete tmp;
        }
    }

    public void AddNode(CrunchNode parent, Crunch cr) {
        CrunchNode nd = new CrunchNode(parent);
        nd.data = cr;
        nd.data.padBytes = nd.data.bytes;
        nd.data.padStop = nd.data.stop;
        nd.data.deep = parent.data.deep + 1;

        parent.dn.addFirst(nd);
        ends.addFirst(nd);
    }

    public void BuildBestList(LinkedList<Block> lst, CrunchNode node) {
        Block blk;
        Block tmp;
        int rem;

        while (node != null) {
            if (node.data.block != null) { // FIXME: Unsure?
                blk = node.data.block.dup();
                if (blk.start == 27) {
                    System.out.println("YE");
                }
                blk.shrink(node.data.start, node.data.stop);

                while (blk.len > pTypeLimits[blk.type].lenMax) {
                    rem = blk.len % pTypeLimits[blk.type].lenMax;
                    if (rem == 0) {
                        rem = pTypeLimits[blk.type].lenMax;
                    }

                    tmp = blk.dup();

                    blk.dropLen(blk.len - rem);
                    tmp.shrink(blk.stop, tmp.stop);

                    lst.remove(tmp);
                    lst.addFirst(tmp);
                }

                if (blk.len >= blk.getMinLength()) {
                    lst.remove(blk);
                    lst.addFirst(blk);
                }
            }

            node = node.parent;
        }
    }
}
