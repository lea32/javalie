# javalie
Simple helper for testing JSR-303 validation on your entities.

sample code:

```
import static ru.leasoft.javalie.Javalie.*;

//in test method
...
SampleEntity e = expectConstraintViolationExceptionWhen(yourPersistenceService::save, invalidValueForTest);

assertThatField(e.getFieldWithNotNullAnnotationOnIt()).violatesConstraint(NotNull.class);
```
