import com.com.fail.ExpectedFailure;
import com.com.fail.ShouldFail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ExpectedFailure.class)
public class ExpectedFailureTest {

    @ShouldFail
    @Test
    void testWhichShouldFail() {
        fail("I was told to");
    }

    @Test
    void testWhichShouldntFail() {
        assertTrue(true);
    }
}
