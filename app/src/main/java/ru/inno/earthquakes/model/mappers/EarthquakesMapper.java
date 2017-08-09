package ru.inno.earthquakes.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

import ru.inno.earthquakes.entities.Earthquake;
import ru.inno.earthquakes.entities.Location;
import ru.inno.earthquakes.model.models.db.CoordinatesDb;
import ru.inno.earthquakes.model.models.db.EarthquakeDb;
import ru.inno.earthquakes.model.models.db.LocationDb;
import ru.inno.earthquakes.model.models.network.EarthquakeNetwork;

/**
 * Maps Earthquakes entities back and forth from business to network and db models
 *
 * @author Artur Badretdinov (Gaket)
 *         05.08.17
 */
@Mapper
public abstract class EarthquakesMapper {

    public abstract Earthquake earthquakeToEntity(EarthquakeDb earthquakeDb);

    public abstract EarthquakeDb entityToDb(Earthquake earthquakes);

    public abstract List<Earthquake> earthquakesToEntities(List<EarthquakeDb> earthquakeDbs);

    public abstract List<EarthquakeDb> entitiesToDb(List<Earthquake> earthquakes);

    public abstract LocationDb loc(Location loc);

    public abstract CoordinatesDb coords(Location.Coordinates coordinates);

    public Location.Coordinates arrayToCoords(double[] coords) {
        // coordinates come in order: longtitude, latitude, depth
        return new Location.Coordinates(coords[1], coords[0]);
    }

    public Location.Coordinates coords(CoordinatesDb coordinatesDb) {
        return new Location.Coordinates(coordinatesDb.getLatitude(), coordinatesDb.getLongtitude());
    }

    public Location loc(LocationDb loc) {
        Location location = new Location();
        location.setCoords(coords(loc.getCoords()));
        location.setName(loc.getName());
        return location;
    }

    @Mappings({
            @Mapping(source = "properties.title", target = "title"),
            @Mapping(source = "properties.time", target = "time"),
            @Mapping(source = "properties.mag", target = "magnitude"),
            @Mapping(source = "properties.url", target = "detailsUrl"),
            @Mapping(source = "properties.place", target = "location.name"),
            @Mapping(source = "geometry.coordinates", target = "location.coords")
    })
    public abstract Earthquake earthquakeDataToEntity(EarthquakeNetwork earthquakeNetwork);
}
