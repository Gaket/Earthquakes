package ru.inno.earthquakes.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

import ru.inno.earthquakes.entities.EarthquakeEntity;
import ru.inno.earthquakes.entities.Location;
import ru.inno.earthquakes.model.dbobjects.Coordinates;
import ru.inno.earthquakes.model.dbobjects.DbLocation;
import ru.inno.earthquakes.model.dbobjects.Earthquake;
import ru.inno.earthquakes.model.earthquakes.rawmodels.EarthquakeDataModel;

/**
 * @author Artur Badretdinov (Gaket)
 *         05.08.17
 */
@Mapper
public abstract class EarthquakesMapper {

    public abstract EarthquakeEntity earthquakeToEntity(Earthquake car);

    public abstract Earthquake entityToDb(EarthquakeEntity earthquakes);

    public abstract List<EarthquakeEntity> earthquakesToEntities(List<Earthquake> earthquakes);

    public abstract List<Earthquake> entitiesToDb(List<EarthquakeEntity> earthquakes);

    @Mappings({
            @Mapping(source = "properties.title", target = "title"),
            @Mapping(source = "properties.time", target = "time"),
            @Mapping(source = "properties.mag", target = "magnitude"),
            @Mapping(source = "properties.url", target = "detailsUrl"),
            @Mapping(source = "properties.place", target = "location.name"),
            @Mapping(source = "geometry.coordinates", target = "location.coords")
    })
    public abstract EarthquakeEntity earthquakeDataToEntity(EarthquakeDataModel car);

    public Location.Coordinates arrayToCoords(double[] coords) {
        return new Location.Coordinates(coords[0], coords[1]);
    }

    public Location.Coordinates coords(Coordinates coordinates) {
        return new Location.Coordinates(coordinates.getLongtitude(), coordinates.getLatitude());
    }

    public Location loc(DbLocation loc) {
        Location location = new Location();
        location.setCoords(coords(loc.getCoords()));
        location.setName(loc.getName());
        return location;
    }

    public abstract DbLocation loc(Location loc);

    public abstract Coordinates coords(Location.Coordinates coordinates);
}
