package gt.edu.umg.repository;

import org.tree.engine.model.CategoryNode;
import org.springframework.stereotype.Repository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;


@Repository
@ConditionalOnProperty(name = "app.storage", havingValue = "postgres")
public class PostgresRepository implements TreeStorageRepository {

    private final JdbcTemplate jdbc;

    public PostgresRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public CategoryNode save(CategoryNode node) {
        jdbc.update(
            "INSERT INTO nodes (id, name, description, parent_id) VALUES (?, ?, ?, ?)",
            node.getId(), node.getName(), node.getDescription(), node.getParentId()
        );
        return node;
    }

    @Override
    public List<CategoryNode> findAll() {
        return jdbc.query("SELECT * FROM nodes", rowMapper());
    }

    @Override
    public Optional<CategoryNode> findById(String id) {
        List<CategoryNode> result = jdbc.query(
            "SELECT * FROM nodes WHERE id = ?", rowMapper(), id
        );
        return result.stream().findFirst();
    }

    private RowMapper<CategoryNode> rowMapper() {
        return (rs, rowNum) -> {
            CategoryNode node = new CategoryNode();
            node.setId(rs.getString("id"));
            node.setName(rs.getString("name"));
            node.setDescription(rs.getString("description"));
            node.setParentId(rs.getString("parent_id"));
            return node;
        };
    }
}
