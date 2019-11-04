package ru.inno.earthquakes.models.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.inno.earthquakes.models.db.CoordinatesDb;
import ru.inno.earthquakes.models.db.EarthquakeDb;
import ru.inno.earthquakes.models.db.LocationDb;
import ru.inno.earthquakes.models.entities.Earthquake;
import ru.inno.earthquakes.models.entities.Location;
import ru.inno.earthquakes.models.entities.News;
import ru.inno.earthquakes.models.network.ArticleNetwork;

/**
 * Maps News entities back and forth from business to network and db models
 *
 * @author Artur Badretdinov (Gaket) 05.08.17
 */
@Mapper
public abstract class NewsMapper {

  @Mappings({
      @Mapping(source = "urlToImage", target = "picture"),
  })
  public abstract News newsDataToEntity(ArticleNetwork articleNetwork);
}
