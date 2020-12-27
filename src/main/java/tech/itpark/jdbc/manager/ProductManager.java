package tech.itpark.jdbc.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.itpark.jdbc.exception.DataAccessException;
import tech.itpark.jdbc.exception.NotFoundException;
import tech.itpark.jdbc.model.Flat;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FlatManager {
    private final DataSource dataSource;

    public List<Flat> getAll() {
        try (
                Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT id, owner_id, district, price, rooms FROM flats order by id LIMIT 50")
        ) {

            List<Flat> items = new ArrayList<>();
            while (rs.next()) {
                items.add(mapRow(rs));

            }

            return items;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }


    public Flat getById(long id) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT id,owner_id,district, price, rooms FROM flats WHERE id = ?");
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

    public List<Flat> getByOwnerId(long ownerId) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT id,owner_id,district, price, rooms FROM flats WHERE owner_id = ?");
        ) {
            stmt.setLong(1, ownerId);
            ResultSet rs = stmt.executeQuery();

            List<Flat> items = new ArrayList<>();
            while (rs.next()) {
                items.add(mapRow(rs));

            }

            return items;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public Flat save(Flat item) {
        if (item.getId() == 0) {
            try (
                    Connection connection = dataSource.getConnection();
                    PreparedStatement stmt = connection.prepareStatement(
                            "INSERT INTO flats(owner_id, district, price, rooms) VALUES (?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
            ) {
                int index = 0;
                stmt.setLong(++index, item.getOwner_id());
                stmt.setString(++index, item.getDistrict());
                stmt.setInt(++index, item.getPrice());
                stmt.setInt(++index, item.getRooms());

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
                PreparedStatement stmt = connection.prepareStatement("UPDATE flats SET owner_id = ?, district = ?, price = ?, rooms = ? WHERE id = ?");
        ) {
            int index = 0;
            stmt.setLong(++index, item.getOwner_id());
            stmt.setString(++index, item.getDistrict());
            stmt.setInt(++index, item.getPrice());
            stmt.setInt(++index, item.getRooms());
            stmt.setLong(++index, item.getId());

            stmt.execute();

            return getById(item.getId());
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public Flat removeById(long id) {
        Flat item = getById(id);

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM flats WHERE id = ?");
        ) {
            stmt.setLong(1, id);
            stmt.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return item;
    }

    private Flat mapRow(ResultSet rs) throws SQLException {
        return new Flat(
                rs.getLong("id"),
                rs.getLong("owner_id"),
                rs.getString("district"),
                rs.getInt("price"),
                rs.getInt("rooms")
        );
    }
}
