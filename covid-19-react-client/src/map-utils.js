
export class MapUtils {

  static calculateColor(maximal, value, opacity) {
    let percent = value / maximal * 100;
    let green = percent < 50 ? 255 : Math.floor(255 - (percent * 2 - 100) * 255 / 100);
    let red = percent > 50 ? 255 : Math.floor((percent * 2) * 255 / 100);
    return 'rgba(' + red + ', ' + green + ', 0, ' + opacity + ')';
  }
}