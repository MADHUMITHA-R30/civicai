import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-before-after-slider',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './before-after-slider.component.html',
  styleUrls: ['./before-after-slider.component.css']
})
export class BeforeAfterSliderComponent {
  @Input() beforeImage: string = '';
  @Input() afterImage: string = '';
  @Input() beforeLabel: string = 'BEFORE (Reported Issue)';
  @Input() afterLabel: string = 'AFTER (Resolved Work)';

  sliderPosition: number = 50;

  onSliderChange(event: any) {
    this.sliderPosition = event.target.value;
  }
}
