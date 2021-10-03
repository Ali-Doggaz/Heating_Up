import React,{useEffect} from 'react';
import Form from './pages/Form';
import { Route, Switch } from 'react-router';
import HomePage from './pages/HomePage';
import { useHistory } from 'react-router';
import Search from './pages/Search';
import TransitionScreen from './components/TransitionScreen';
import { CSSTransition } from 'react-transition-group';

const routes = [
  { path:"/search",Component:Search, name:'search'},
  { path:"/home", Component:HomePage, name:"home" },
  { path:"/", Component:Form, name:'form'},
]

function App() {
  const history = useHistory();
  useEffect(()=>{
    if(localStorage.getItem('userData'))
      history.push("/home")
  })


  return (
    <div className="App">
      <div className="background"></div>
     
      
      {
        routes.map(({path,Component,name})=>(
          <Route path={path} exact key={name}>
            {({match})=>(
              <CSSTransition
                in={match != null}
                timeout={1300}
                classNames="page"
                unmountOnExit
              >
                <div className="page">
                  <TransitionScreen/>
                  <Component/>
                </div>
              </CSSTransition>
            )}
            
          </Route>
        ))
      }
      
      
    </div>
  );
}

export default App;