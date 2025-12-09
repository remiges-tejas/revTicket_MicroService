import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { MovieService } from './movie.service';
import { BookingService } from './booking.service';
import { UserService } from './user.service';
import { TheaterService } from './theater.service';
import { ShowtimeService } from './showtime.service';

export interface DashboardStats {
  totalMovies: number;
  totalBookings: number;
  totalRevenue: number;
  totalUsers: number;
  todayBookings: number;
  cancelledBookings: number;
  activeMovies: number;
}

export interface RevenueData {
  date: string;
  revenue: number;
}

export interface RecentActivity {
  id: string;
  type: 'booking' | 'movie' | 'user' | 'theater';
  message: string;
  timestamp: Date;
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private http = inject(HttpClient);
  private movieService = inject(MovieService);
  private bookingService = inject(BookingService);
  private userService = inject(UserService);
  private theaterService = inject(TheaterService);
  private showtimeService = inject(ShowtimeService);

  getDashboardStats(): Observable<DashboardStats> {
    return this.http.get<any>(`${environment.apiUrl}/admin/dashboard/overview`).pipe(
      map(overview => ({
        totalMovies: overview.totalMovies || 0,
        totalBookings: overview.totalBookings || 0,
        totalRevenue: overview.totalRevenue || 0,
        totalUsers: overview.totalUsers || 0,
        todayBookings: overview.activeBookings || 0, // Map activeBookings to todayBookings
        cancelledBookings: overview.cancelledBookings || 0,
        activeMovies: overview.totalMovies || 0 // Assume all movies are active for now
      }))
    );
  }

  getRevenueData(days: number = 7): Observable<RevenueData[]> {
    // Use the /admin/dashboard/revenue endpoint which returns revenue stats
    return this.http.get<any>(`${environment.apiUrl}/admin/dashboard/revenue`).pipe(
      map(revenueStats => {
        // Generate data points based on the period
        const data: RevenueData[] = [];
        const today = new Date();
        const totalRevenue = revenueStats.totalRevenue || 0;

        // Generate sample data distributed over the period
        // In production, this should come from the backend with actual daily/weekly data
        for (let i = days - 1; i >= 0; i--) {
          const date = new Date(today);
          date.setDate(today.getDate() - i);
          // Distribute revenue with some variation (this is a placeholder)
          const dailyRevenue = totalRevenue / days + (Math.random() - 0.5) * (totalRevenue / days / 2);
          data.push({
            date: date.toISOString().split('T')[0],
            revenue: Math.max(0, dailyRevenue)
          });
        }
        return data;
      })
    );
  }

  getRecentActivity(limit: number = 10): Observable<RecentActivity[]> {
    return forkJoin({
      bookings: this.bookingService.getAllBookings(),
      movies: this.movieService.getMovies()
    }).pipe(
      map(({ bookings, movies }) => {
        const activities: RecentActivity[] = [];

        // Recent bookings
        bookings
          .sort((a, b) => new Date(b.bookingDate).getTime() - new Date(a.bookingDate).getTime())
          .slice(0, 5)
          .forEach(booking => {
            activities.push({
              id: booking.id,
              type: 'booking',
              message: `New booking for ${booking.movieTitle}`,
              timestamp: new Date(booking.bookingDate)
            });
          });

        // Sort by timestamp and return limited results
        return activities
          .sort((a, b) => b.timestamp.getTime() - a.timestamp.getTime())
          .slice(0, limit);
      })
    );
  }

  getPopularMovies(limit: number = 5): Observable<any[]> {
    return forkJoin({
      movies: this.movieService.getMovies(),
      bookings: this.bookingService.getAllBookings()
    }).pipe(
      map(({ movies, bookings }) => {
        const movieStats = new Map<string, { movie: any; bookings: number }>();

        movies.forEach(movie => {
          movieStats.set(movie.id, {
            movie,
            bookings: 0
          });
        });

        bookings.forEach(booking => {
          const stat = movieStats.get(booking.movieId);
          if (stat) {
            stat.bookings++;
          }
        });

        return Array.from(movieStats.values())
          .sort((a, b) => b.bookings - a.bookings)
          .slice(0, limit)
          .map(stat => ({
            ...stat.movie,
            bookingCount: stat.bookings
          }));
      })
    );
  }
}

