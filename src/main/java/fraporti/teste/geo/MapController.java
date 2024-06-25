package fraporti.teste.geo;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
public class MapController {
  private final MapRepo mapRepo;

  public MapController(MapRepo mapRepo) {
    this.mapRepo = mapRepo;
  }

  @PostMapping("/add")
  public void addMap(@RequestBody MapDTO map) {
    MapModel mapa = new MapModel();
    GeometryFactory gf = new GeometryFactory();
    Coordinate[] coords = new Coordinate[map.polygon().size()];
    for (int i = 0; i < map.polygon().size(); i++) {
      List<Double> point = map.polygon().get(i);
      coords[i] = new Coordinate(point.get(0), point.get(1));
    }
    mapa.setPolygon(gf.createPolygon(coords));
    mapRepo.save(mapa);
  }

  @GetMapping("/all")
  public List<MapDTO> getAllMaps() {
    List<MapModel> polis = mapRepo.findAll();
    List<MapDTO> maps = new ArrayList<>();
    for (MapModel map : polis) {
      List<List<Double>> polygon = new ArrayList<>();
      for (Coordinate coord : map.getPolygon().getCoordinates()) {
        polygon.add(List.of(coord.getX(), coord.getY()));
      }
      maps.add(new MapDTO(map.getId(), polygon));
    }
    return maps;
  }

  @PostMapping("/addMP")
  public void addMultiPolig(@RequestBody MultiPoli mp) {
      GeometryFactory gf = new GeometryFactory();
      Polygon[] polygons = new Polygon[mp.polygons().size()];
      for (int i = 0; i < mp.polygons().size(); i++) {
        List<List<Double>> polygon = mp.polygons().get(i);
        Coordinate[] coords = new Coordinate[polygon.size()];
        for (int j = 0; j < polygon.size(); j++) {
          List<Double> point = polygon.get(j);
          coords[j] = new Coordinate(point.get(0), point.get(1));
        }
        polygons[i] = gf.createPolygon(coords);
      }
      System.out.println(polygons);
  }
  

  public record MapDTO(
    Long id,
    List<List<Double>> polygon
  ) { }

  public record MultiPoli(
    List<List<List<Double>>> polygons
  ) {}
}
