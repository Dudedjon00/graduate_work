package tech.itpark.jdbc.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.itpark.jdbc.exception.DataAccessException;
import tech.itpark.jdbc.exception.NotFoundException;
import tech.itpark.jdbc.model.Products;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductManager {
    private final DataSource dataSource;

    public List<Products> getAll() {
        try (
                Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT id, worker_id, items, price, quantity FROM Products order by id LIMIT 50")
        ) {

            List<Products> items = new ArrayList<>();
            while (rs.next()) {
                items.add(mapRow(rs));

            }

            return items;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }


    public Products getById(long id) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT id,worker_id,items, price, quantity FROM Products WHERE id = ?");
        ) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapRow(rs);

            }

            throw new NotFoundException();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public List<Products> getByworkerId(long workerId) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT id,worker_id,items, price, quantity FROM Products WHERE worker_id = ?");
        ) {
            stmt.setLong(1, workerId);
            ResultSet rs = stmt.executeQuery();

            List<Products> items = new ArrayList<>();
            while (rs.next()) {
                items.add(mapRow(rs));

            }

            return items;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public Products save(Products item) {
        if (item.getId() == 0) {
            try (
                    Connection connection = dataSource.getConnection();
                    PreparedStatement stmt = connection.prepareStatement(
                            "INSERT INTO Products(worker_id, items, price, quantity) VALUES (?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
            ) {
                int index = 0;
                stmt.setLong(++index, item.getWorkers_id());
                stmt.setString(++index, item.getItems());
                stmt.setInt(++index, item.getPrice());
                stmt.setInt(++index, item.getQuantity());

                stmt.execute();

                try (ResultSet keys = stmt.getGeneratedKeys();) {
                    if (keys.next()) {
                        long id = keys.getLong(1);
                        return getById(id);
                    }

                    throw new DataAccessException("No keys generated");
                }
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }

        }

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement("UPDATE Products SET worker_id = ?, items = ?, price = ?, quantity = ? WHERE id = ?");
        ) {
            int index = 0;
            stmt.setLong(++index, item.getWorkers_id());
            stmt.setString(++index, item.getItems());
            stmt.setInt(++index, item.getPrice());
            stmt.setInt(++index, item.getQuantity());
            stmt.setLong(++index, item.getId());

            stmt.execute();

            return getById(item.getId());
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public Products removeById(long id) {
        Products item = getById(id);

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM Products WHERE id = ?");
        ) {
            stmt.setLong(1, id);
            stmt.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return item;
    }

    private Products mapRow(ResultSet rs) throws SQLException {
        return new Products(
                rs.getLong("id"),
                rs.getLong("worker_id"),
                rs.getString("items"),
                rs.getInt("price"),
                rs.getInt("quantity")
        );
    }
}
