import MainLayout from 'Frontend/views/MainLayout.js';
import { lazy } from 'react';
import { createBrowserRouter, RouteObject } from 'react-router-dom';

const AboutView = lazy(async () => import('Frontend/views/about/AboutView.js'));
const HomeView = lazy(async () => import('Frontend/views/home/HomeView.js'));

const AiView = lazy(async () => import('Frontend/views/ai/AiView.js'));
const CloView = lazy(async () => import('Frontend/views/clo/CloView.js'));
const ReportsView = lazy(async () => import('Frontend/views/reports/ReportsView.js'));
const TradesView = lazy(async () => import('Frontend/views/trades/TradesView.js'));

const routing = [
  {
    element: <MainLayout />,
    handle: { title: 'Main' },
    children: [
      { path: '/', element: <HomeView />, handle: { title: 'Welcome to Chat With CLO ' } },
      { path: '/ai', element: <AiView />, handle: { title: 'Plain Ai' } },
      { path: '/clo', element: <CloView />, handle: { title: 'Chat with Clo' } },
      { path: '/reports', element: <ReportsView />, handle: { title: 'Reports' } },
      { path: '/trades', element: <TradesView />, handle: { title: 'Trades' } },
      { path: '/about', element: <AboutView />, handle: { title: 'About' } },
    ],
  },
] as RouteObject[];

export const routes = routing;
export default createBrowserRouter(routes);
