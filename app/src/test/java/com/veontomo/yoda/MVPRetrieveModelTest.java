package com.veontomo.yoda;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * Tests for the retrieval model.
 */
public class MVPRetrieveModelTest {

    private MVPRetrieveModel model;

    @Before
    public void init() {
        model = new MVPRetrieveModel();
    }

    @Test
    public void testSetCategoryStatusesToTrueTrue() {
        model.setCategoryStatuses(new boolean[]{true, true});
        assertTrue(model.getCategoryStatus(0));
        assertTrue(model.getCategoryStatus(1));
    }

    @Test
    public void testSetCategoryStatusToFalseTrue() {
        model.setCategoryStatuses(new boolean[]{false, true});
        assertFalse(model.getCategoryStatus(0));
        assertTrue(model.getCategoryStatus(1));
    }

    @Test
    public void testSetCategoryStatusToTrueFalse() {
        model.setCategoryStatuses(new boolean[]{true, false});
        assertTrue(model.getCategoryStatus(0));
        assertFalse(model.getCategoryStatus(1));
    }

    @Test
    public void testSetCategoryStatusToFalseFalse() {
        model.setCategoryStatuses(new boolean[]{false, false});
        assertFalse(model.getCategoryStatus(0));
        assertFalse(model.getCategoryStatus(1));
    }



    @Test
    public void testGetCategoryReturnsRandomIfBothStatusesAreFalse() {
        MVPRetrieveModel spy = Mockito.spy(model);
        model.setCategoryStatuses(new boolean[]{false, false});
        spy.getCategoryToRetrieve();
        verify(spy, times(1)).getRandomCategory();
    }

    @Test
    public void testGetRandomCategoryWhenRuns100Times(){
        final boolean[] results = new boolean[]{false, false};
        int maxIterations = 10;
        int i = 0;
        String category;
        do {
            category = model.getRandomCategory();
            if ("movie".equals(category)) {
                results[0] = true;
            } else if ("famous".equals(category)){
                results[1] = true;
            }
            i++;
        } while (i < maxIterations && !(results[0] && results[1]));
        assertTrue("in " + maxIterations + " iteration it was supposed to see at least one \"movie\"", results[0]);
        assertTrue("in " + maxIterations + " iteration it was supposed to see at least one \"famous\"", results[1]);
    }

}