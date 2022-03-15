package com.ufipay.eummhub.core.fragments;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class FragmentConnexionTest {

    @Mock
    FragmentConnexion fragmentConnexion;

    @Before
    public void setup(){
        fragmentConnexion = new FragmentConnexion();
    }

    @Test
    public void connectionFormValid() {
        assertTrue(fragmentConnexion.connectionFormValid("237697240704", "2018"));
    }
}