package ru.inno.earthquakes.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

import ru.inno.earthquakes.entities.Earthquake;
import ru.inno.earthquakes.entities.Location;
import ru.inno.earthquakes.model.models.dbobjects.CoordinatesDb;
import ru.inno.earthquakes.model.models.dbobjects.EarthquakeDb;
import ru.inno.earthquakes.model.models.dbobjects.LocationDb;
import ru.inno.earthquakes.model.models.network.EarthquakeNetwork;

/**
 * @author Artur Badretdinov (Gaket)
 *         05.08.17
 */
@Mapper
public abstract class EarthquakesMapper {

    public abstract Earthquake earthquakeToEntity(EarthquakeDb car);

    public abstract EarthquakeDb entityToDb(Earthquake earthquakes);

    public abstract List<Earthquake> earthquakesToEntities(List<EarthquakeDb> earthquakeDbs);

    public abstract List<EarthquakeDb> entitiesToDb(List<Earthquake> earthquakes);

    @Mappings({
            @Mapping(source = "properties.title", target = "title"),
            @Mapping(source = "properties.time", target = "time"),
            @Mapping(source = "properties.mag", target = "magnitude"),
            @Mapping(source = "properties.url", target = "detailsUrl"),
            @Mapping(source = "properties.place", target = "location.name"),
            @Mapping(source = "geometry.coordinates", target = "location.coords")
    })
    public abstract Earthquake earthquakeDataToEntity(EarthquakeNetwork car);

    public Location.Coordinates arrayToCoords(double[] coords) {
        return new Location.Coordinates(coords[0], coords[1]);
    }

    public Location.Coordinates coords(CoordinatesDb coordinatesDb) {
        return new Location.Coordinates(coordinatesDb.getLongtitude(), coordinatesDb.getLatitude());
    }

    public Location loc(LocationDb loc) {
        Location location = new Location();
        location.setCoords(coords(loc.getCoords()));
        location.setName(loc.getName());
        return location;
    }

    public abstract LocationDb loc(Location loc);

    public abstract CoordinatesDb coords(Location.Coordinates coordinates);
}
