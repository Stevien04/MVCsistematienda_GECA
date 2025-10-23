<style>
    :root {
        --primary-color: #667eea;
        --secondary-color: #764ba2;
        --sidebar-width: 280px;
    }
    
    body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background-color: #f8f9fa;
        margin: 0;
        padding: 0;
        overflow-x: hidden;
    }
    
    .sidebar {
        background: linear-gradient(180deg, var(--primary-color) 0%, var(--secondary-color) 100%);
        color: white;
        width: var(--sidebar-width);
        height: 100vh;
        position: fixed;
        transition: all 0.3s;
        box-shadow: 3px 0 15px rgba(0,0,0,0.1);
        z-index: 1000;
        left: 0;
        top: 0;
    }
    
    .sidebar-brand {
        padding: 1.5rem 1rem;
        border-bottom: 1px solid rgba(255,255,255,0.1);
        text-align: center;
    }
    
    .sidebar .nav-link {
        color: rgba(255,255,255,0.9);
        padding: 0.8rem 1.5rem;
        margin: 0.1rem 1rem;
        border-radius: 10px;
        transition: all 0.3s;
        font-weight: 500;
        text-decoration: none;
        display: block;
    }
    
    .sidebar .nav-link:hover {
        background: rgba(255,255,255,0.15);
        color: white;
        transform: translateX(5px);
    }
    
    .sidebar .nav-link.active {
        background: rgba(255,255,255,0.2);
        color: white;
        box-shadow: 0 4px 15px rgba(0,0,0,0.1);
    }
    
    .main-content {
        margin-left: var(--sidebar-width);
        transition: all 0.3s;
        min-height: 100vh;
        background-color: #f8f9fa;
    }
    
    .navbar-custom {
        background: white;
        box-shadow: 0 2px 15px rgba(0,0,0,0.1);
        padding: 1rem 1.5rem;
        position: sticky;
        top: 0;
        z-index: 999;
    }
    
    .sidebar-collapsed {
        width: 80px;
        transform: translateX(0);
    }
    
    .sidebar-collapsed .sidebar-brand h4,
    .sidebar-collapsed .sidebar-brand small,
    .sidebar-collapsed .nav-link span {
        display: none;
    }
    
    .sidebar-collapsed .nav-link {
        text-align: center;
        padding: 0.8rem 0.5rem;
        margin: 0.2rem 0.5rem;
    }
    
    .sidebar-collapsed .nav-link i {
        margin-right: 0;
        font-size: 1.2rem;
    }
    
    .main-content-expanded {
        margin-left: 80px;
    }
    
    @media (max-width: 768px) {
        .sidebar {
            width: var(--sidebar-width);
            transform: translateX(-100%);
        }
        
        .sidebar.show {
            transform: translateX(0);
        }
        
        .main-content {
            margin-left: 0;
        }
        
        .sidebar-collapsed {
            transform: translateX(-100%);
        }
        
        .main-content-expanded {
            margin-left: 0;
        }
    }
</style>