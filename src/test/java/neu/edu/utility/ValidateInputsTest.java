/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neu.edu.utility;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

/**
 *
 * @author aelinadas
 */
@RunWith(MockitoJUnitRunner.class)
public class ValidateInputsTest {

    @Mock
    ValidateInputs vi;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateFieldTest() {
        //create test parameters
        String name = "myuser";

        try {
            vi = new ValidateInputs();
            boolean actual = vi.isName(name);
            assertTrue(actual);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
