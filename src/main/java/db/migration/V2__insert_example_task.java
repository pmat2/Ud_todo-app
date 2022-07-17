package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class V2__insert_example_task extends BaseJavaMigration {

    @Override
    public void migrate(Context context){
        new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true))
                .execute("insert into tasks (description, done) values ('Migration task', true)");
    }
}
