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
    public void testSetCategoryStatusToTrueIfIndexIsZero() {
        model.setCategoryStatus(0, true);
        assertTrue(model.getCategoryStatus(0));
    }

    @Test
    public void testSetCategoryStatusToFalseIfIndexIsZero() {
        model.setCategoryStatus(0, false);
        assertFalse(model.getCategoryStatus(0));
    }

    @Test
    public void testSetCategoryStatusToTrueIfIndexIsOne() {
        model.setCategoryStatus(1, true);
        assertTrue(model.getCategoryStatus(1));
    }

    @Test
    public void testSetCategoryStatusToFalseIfIndexIsOne() {
        model.setCategoryStatus(1, false);
        assertFalse(model.getCategoryStatus(1));
    }

    @Test
    public void testToggleCategoryStatusFromTrueToFalseIfIndexIs0() {
        model.setCategoryStatus(0, true);
        model.toggleCategoryStatus(0);
        assertFalse(model.getCategoryStatus(0));
    }

    @Test
    public void testToggleCategoryStatusFromFalseToTrueIfIndexIs0() {
        model.setCategoryStatus(0, false);
        model.toggleCategoryStatus(0);
        assertTrue(model.getCategoryStatus(0));
    }

    @Test
    public void testToggleCategoryStatusFromTrueToFalseIfIndexIs1() {
        model.setCategoryStatus(1, true);
        model.toggleCategoryStatus(1);
        assertFalse(model.getCategoryStatus(1));
    }

    @Test
    public void testToggleCategoryStatusFromFalseToTrueIfIndexIs1() {
        model.setCategoryStatus(1, false);
        model.toggleCategoryStatus(1);
        assertTrue(model.getCategoryStatus(1));
    }

    @Test
    public void testGetCategoryReturnsMovieIfOnlyItsStatusIsTrue() {
        model.setCategoryStatus(0, true);
        model.setCategoryStatus(1, false);
        assertEquals("movie", model.getCategoryToRetrieve());
    }

    @Test
    public void testGetCategoryReturnsFamousIfOnlyItsStatusIsTrue() {
        model.setCategoryStatus(1, true);
        model.setCategoryStatus(0, false);
        assertEquals("famous", model.getCategoryToRetrieve());
    }


    @Test
    public void testGetCategoryReturnsRandomIfBothStatusesAreTrue() {
        MVPRetrieveModel spy = Mockito.spy(model);
        spy.setCategoryStatus(0, true);
        spy.setCategoryStatus(1, true);
        spy.getCategoryToRetrieve();
        verify(spy, times(1)).getRandomCategory();
    }

    @Test
    public void testGetCategoryReturnsRandomIfBothStatusesAreFalse() {
        MVPRetrieveModel spy = Mockito.spy(model);
        spy.setCategoryStatus(0, false);
        spy.setCategoryStatus(1, false);
        spy.getCategoryToRetrieve();
        verify(spy, times(1)).getRandomCategory();
    }

    @Test
    public void testGetRandomCategoryWhenRuns100Times(){
        final boolean[] results = new boolean[]{false, false};
        int maxIter = 10;
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
        } while (i < maxIter && !(results[0] && results[1]));
        assertTrue("in " + maxIter + " iteration it was supposed to see at least one \"movie\"", results[0]);
        assertTrue("in " + maxIter + " iteration it was supposed to see at least one \"famous\"", results[1]);
    }

}