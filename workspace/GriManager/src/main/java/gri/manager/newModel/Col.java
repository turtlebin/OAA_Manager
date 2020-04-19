package gri.manager.newModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.spark.sql.types.DataType;

@Data
public class Col {
    private String name;
    private DataType type;

    public Col(String name,DataType type){
        this.name=name;
        this.type=type;
    }
}
