import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AnalyticsService } from '../../core/services/analytics.service';
import { DepartmentAnalytics } from '../../core/models/all.models';

@Component({
  selector: 'app-admin-analytics',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-analytics.component.html',
  styleUrls: ['./admin-analytics.component.css']
})
export class AdminAnalyticsComponent implements OnInit {
  analyticsService = inject(AnalyticsService);

  data: DepartmentAnalytics | null = null;
  selectedDeptId: number | undefined = undefined;

  ngOnInit() {
    this.loadAnalytics();
  }

  loadAnalytics() {
    this.analyticsService.getDepartmentAnalytics(this.selectedDeptId).subscribe(res => {
      this.data = res;
    });
  }

  getKeys(obj: any): string[] {
    return obj ? Object.keys(obj) : [];
  }
}
