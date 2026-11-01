import { Routes } from '@angular/router';
import { LandingComponent } from './pages/landing/landing.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { CitizenHomeComponent } from './pages/citizen-home/citizen-home.component';
import { ReportIssueComponent } from './pages/report-issue/report-issue.component';
import { MyReportsComponent } from './pages/my-reports/my-reports.component';
import { ComplaintDetailComponent } from './pages/complaint-detail/complaint-detail.component';
import { DepartmentDashboardComponent } from './pages/department-dashboard/department-dashboard.component';
import { AdminAnalyticsComponent } from './pages/admin-analytics/admin-analytics.component';
import { SuperAdminComponent } from './pages/super-admin/super-admin.component';

export const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'landing', component: LandingComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'citizen-home', component: CitizenHomeComponent },
  { path: 'report', component: ReportIssueComponent },
  { path: 'my-reports', component: MyReportsComponent },
  { path: 'complaint/:id', component: ComplaintDetailComponent },
  { path: 'admin/dashboard', component: DepartmentDashboardComponent },
  { path: 'admin/analytics', component: AdminAnalyticsComponent },
  { path: 'super-admin', component: SuperAdminComponent },
  { path: '**', redirectTo: '' }
];
