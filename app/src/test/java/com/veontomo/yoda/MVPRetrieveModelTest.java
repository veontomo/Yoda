package com.veontomo.yoda;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Tests for the retrieval model.
 *
 *
 */
public class MVPRetrieveModelTest {

    @Test
    public void testSetCategoryStatusToTrueIfIndexIsZero() throws Exception {
        MVPRetrieveModel model = new MVPRetrieveModel();
        model.setCategoryStatus(0, true);
        assertTrue(model.getCategoryStatus(0));
    }

    @Test
    public void testSetCategoryStatusToFalseIfIndexIsZero() throws Exception {
        MVPRetrieveModel model = new MVPRetrieveModel();
        model.setCategoryStatus(0, false);
        assertFalse(model.getCategoryStatus(0));
    }

    @Test
    public void testSetCategoryStatusToTrueIfIndexIsOne() throws Exception {
        MVPRetrieveModel model = new MVPRetrieveModel();
        model.setCategoryStatus(1, true);
        assertTrue(model.getCategoryStatus(1));
    }

    @Test
    public void testSetCategoryStatusToFalseIfIndexIsOne() throws Exception {
        MVPRetrieveModel model = new MVPRetrieveModel();
        model.setCategoryStatus(1, false);
        assertFalse(model.getCategoryStatus(1));
    }

    @Test
    public void testToggleCategoryStatusFromTrueToFalseIfIndexIs0() throws Exception {
        MVPRetrieveModel model = new MVPRetrieveModel();
        model.setCategoryStatus(0, true);
        model.toggleCategoryStatus(0);
        assertFalse(model.getCategoryStatus(0));
    }

    @Test
    public void testToggleCategoryStatusFromFalseToTrueIfIndexIs0() throws Exception {
        MVPRetrieveModel model = new MVPRetrieveModel();
        model.setCategoryStatus(0, false);
        model.toggleCategoryStatus(0);
        assertTrue(model.getCategoryStatus(0));
    }

    @Test
    public void testToggleCategoryStatusFromTrueToFalseIfIndexIs1() throws Exception {
        MVPRetrieveModel model = new MVPRetrieveModel();
        model.setCategoryStatus(1, true);
        model.toggleCategoryStatus(1);
        assertFalse(model.getCategoryStatus(1));
    }

    @Test
    public void testToggleCategoryStatusFromFalseToTrueIfIndexIs1() throws Exception {
        MVPRetrieveModel model = new MVPRetrieveModel();
        model.setCategoryStatus(1, false);
        model.toggleCategoryStatus(1);
        assertTrue(model.getCategoryStatus(1));
    }

}