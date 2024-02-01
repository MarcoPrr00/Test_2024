import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class PropertyProvaTest {
    @Property
    void testProva(@ForAll int a ,@ForAll int b){
        assertEquals(a+b,b+a);
    }

}
