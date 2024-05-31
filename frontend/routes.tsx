import HelloWorldView from 'Frontend/views/helloworld/HelloWorldView.js';
import MainLayout from 'Frontend/views/MainLayout.js';
import { lazy } from 'react';
import { createBrowserRouter, RouteObject } from 'react-router-dom';

const AboutView = lazy(async () => import('Frontend/views/about/AboutView.js'));
const AiView = lazy(async () => import('Frontend/views/ai/AiView.js'));
const CloView = lazy(async () => import('Frontend/views/clo/CloView.js'));

const routing = [
  {
    element: <MainLayout />,
    handle: { title: 'Main' },
    children: [
      { path: '/', element: <HelloWorldView />, handle: { title: 'Hello World' } },
      { path: '/about', element: <AboutView />, handle: { title: 'About' } },
      { path: '/ai', element: <AiView />, handle: { title: 'Ai' } },
      { path: '/clo', element: <CloView />, handle: { title: 'Clo' } },
    ],
  },
] as RouteObject[];

export const routes = routing;
export default createBrowserRouter(routes);
