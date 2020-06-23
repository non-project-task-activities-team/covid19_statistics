
export class MapUtils {

  static calculateColor(maximal, value, opacity) {
    if(maximal === undefined || maximal < 1) {
      maximal = 1; // to avoid "black" map
    }
    let percent = value / maximal * 20000;
    let green = percent < 50 ? 255 : Math.floor(255 - (percent * 2 - 100) * 255 / 100);
    let red = percent > 50 ? 255 : Math.floor((percent * 2) * 255 / 100);
    return 'rgba(' + red + ', ' + green + ', 0, ' + opacity + ')';
  }
}
