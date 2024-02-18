import { Component, Input } from '@angular/core';
import { Badge } from 'src/app/models/Bagde/badge.model';

@Component({
  selector: 'app-badge',
  templateUrl: './badge.component.html',
  styleUrls: ['./badge.component.css']
})
/* this class is useful for designing a single badge */
export class BadgeComponent {
  @Input() badge!: Badge;
  @Input() isUnlocked!: boolean;
}
