import { Badge } from './badge.model';

describe('Badge', () => {
  it('should create an instance', () => {
    expect(new Badge(1, "src", "title", "description", 1)).toBeTruthy();
  });
});
